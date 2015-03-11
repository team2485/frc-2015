
package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.DualEncoder;
import org.usfirst.frc.team2485.util.DummyOutput;
import org.usfirst.frc.team2485.util.ThresholdHandler;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Anoushka Bose
 * @author Patrick Wamsley
 * @author Ben Clark
 * @author Aidan Fay
 * @author Camille Considine
 */
public class DriveTrain {

	private CombinedVictorSP leftDrive, rightDrive, centerDrive; 
	private Solenoid suspension;
	private DualEncoder dualEncoder;
	private Encoder centerEnc;
	public IMU imu;

	//Drive Controls 
	private final double 
		NORMAL_SPEED_RATING = 0.75,
		FAST_SPEED_RATING = 1.0,
		SLOW_SPEED_RATING = 0.5;
	
	private double driveSpeed = SLOW_SPEED_RATING; //asked by drivers to have this setting
	
	private final double TRANSLATE_Y_DEADBAND = 0.2;
	private final double TRANSLATE_X_DEADBAND = 0.25;
	private final double ROTATION_DEADBAND = 0.2;
	
	private boolean slowStrafeOnlyMode = false, forcedNoStrafeMode = false; 
	private boolean isQuickTurn = false;
	
	private final double  SENSITIVITY_LOW = 0.55, SENSITIVITY_HIGH = 0.85;
	private double quickStopAccumulator = 0.0;
	private double oldWheel = 0.0;

	private static final double SLOW_STRAFE_SCALAR = 0.6; //need to tune
	private static final double STRAFE_TUNING_PARAMETER = 1;
	
	//PID 
	public PIDController driveStraightPID;
	public PIDController imuPID;
	private PIDController strafePID; 
	  
	public double desiredHeading = 0.0; 
	public boolean maintainingHeading = false; //use for auto and while !rotating
	
	private DummyOutput dummyEncoderOutput;
	private DummyOutput dummyImuOutput;

	private double lowEncRate = 40;
	private int imuOnTargetCounter = 0;
	private final int MINIMUM_IMU_ON_TARGET_ITERATIONS = 6;

	private final double
		absTolerance_Imu_TurnTo = 1.0,
		absTolerance_Imu_DriveStraight = 2.0,
		absTolerance_Enc_DriveStraight = 3.0, // needs tuning
		absTolerance_Enc_Strafe = 3.0;
	
	public static double 
		driveStraightEncoder_Kp = 0.0275,
		driveStraightEncoder_Ki = 0.0, 
		driveStraightEncoder_Kd = 0.0;

	public static double 
		strafeEncoder_Kp = 0.005,
		strafeEncoder_Ki = 0.0,
		strafeEncoder_Kd = 0.0;

	public static double
		driveStraightImu_Kp = 0.025, // old data?? seems stale - 0.05 - for floor work, 0.07 for bump (tentatively)
		driveStraightImu_Ki = 0.0,
		driveStraightImu_Kd = 0.01; 

	public static double
		rotateImu_kP = 0.0125,
		rotateImu_kI = 0.00,
		rotateImu_kD = 0.01;
	
	//Anti-tipping 
	private double oldXInput, oldYInput; 
	
	private static double MAX_DELTA_X = 0.02; 
	private static double MAX_DELTA_Y_NORMAL = 0.05, MAX_DELTA_Y_DANGER = 0.025; 

	public DriveTrain(CombinedVictorSP leftDrive, CombinedVictorSP rightDrive, CombinedVictorSP center, Solenoid suspension, 
			IMU imu, Encoder leftEnc, Encoder rightEnc, Encoder centerEnc) {

		this.leftDrive 		= leftDrive; 
		this.rightDrive     = rightDrive; 
		this.centerDrive	= center;
		this.suspension 	= suspension;
		this.imu            = imu;
		this.centerEnc		= centerEnc;
		this.dualEncoder	= new DualEncoder(leftEnc, rightEnc); 

		if (this.imu != null) 
			setImu(this.imu);
		
		dummyEncoderOutput = new DummyOutput();
		driveStraightPID = new PIDController(driveStraightEncoder_Kp, driveStraightEncoder_Ki, driveStraightEncoder_Kd, dualEncoder, dummyEncoderOutput);
		driveStraightPID.setAbsoluteTolerance(absTolerance_Enc_DriveStraight);

		if (centerEnc != null) {
			strafePID = new PIDController(strafeEncoder_Kp, strafeEncoder_Ki, strafeEncoder_Kd, centerEnc, center);
			strafePID.setAbsoluteTolerance(absTolerance_Enc_Strafe);
		}
	}

	public void warlordDrive(double translateX, double translateY, double rotation) {
		
		translateX = ThresholdHandler.deadbandAndScale(translateX, TRANSLATE_X_DEADBAND, 0, 1); //TODO:  min prob wont be zero. 
		translateY = -ThresholdHandler.deadbandAndScale(translateY, TRANSLATE_Y_DEADBAND, 0, 1);
		rotation   =  ThresholdHandler.deadbandAndScale(rotation, ROTATION_DEADBAND, 0, 1);
		
		
		if (slowStrafeOnlyMode) {
			translateY = 0; 
			translateX *= SLOW_STRAFE_SCALAR; 
			rotation = 0; //no rotation if we only want to strafe
		} else if (forcedNoStrafeMode) {
			translateX = 0; 
			translateY *= 1; //drivers prefer to have it this way rather than a 1.25X boost. 
			rotation = 0; //no rotation if we only want to move forward
		}
		
		//clamp
		if (translateY > 1)
			translateY = 1; 
		else if (translateY < -1)
			translateY = -1; 
		
		//prevents tipping from too much acc
		double dXInput = Math.abs(translateX - oldXInput), dYInput = Math.abs(translateY - oldYInput); 

		if (dXInput > MAX_DELTA_X) {

			if (translateX > oldXInput)
				translateX = oldXInput + MAX_DELTA_X; 
			else
				translateX = oldXInput - MAX_DELTA_X; 

			if (translateX > 1)
				translateX = 1;
			else if (translateX < -1)
				translateX = -1;
		}
		
		double currMaxDeltaY = translateY > oldYInput && oldYInput < 0 ? MAX_DELTA_Y_DANGER : MAX_DELTA_Y_NORMAL; 

		if (dYInput > currMaxDeltaY) {

			if (translateY > oldYInput)
				translateY = oldYInput + currMaxDeltaY; 
			else
				translateY = oldYInput - currMaxDeltaY; 

			if(translateY > 1)
				translateY = 1;
			else if (translateY < -1)
				translateY = -1;
		}

		oldXInput = translateX;
		oldYInput = translateY;

		if (Math.abs(rotation) > 0.1) {
			if (maintainingHeading) {
				maintainingHeading = false; 
				imuPID.disable(); 
			}
			rotationalDrive(translateY, rotation);
		} else {
			if (!maintainingHeading) {
				maintainingHeading = true; 
				desiredHeading = imu.getYaw(); 

				setImuForDrivingStraight(); 
				imuPID.setSetpoint(desiredHeading);
				imuPID.enable();
			}
			strafeDrive(translateX, translateY);
		}
	}

	public void rotationalDrive(double translateY, double rotation) {

		dropCenterWheel(false);
		setCenterWheel(0);
		oldXInput = 0; 

		double rightDriveOutput, leftDriveOutput;
		
		rightDriveOutput = translateY - rotation; 
		leftDriveOutput = translateY + rotation; //check signs
		
		//tune values a bit. maybe muliply rotation by translateY (when translateY != 0) 
		
		//clamp
		if (rightDriveOutput > 1)
			rightDriveOutput = 1; 
		if (rightDriveOutput < -1)
			rightDriveOutput = -1; 
		if (leftDriveOutput > 1)
			leftDriveOutput = 1;
		if (leftDriveOutput < -1)
			leftDriveOutput = -1; 
		
		setLeftRight(leftDriveOutput, rightDriveOutput);
	}

	public void setImuForDrivingStraight() {
		imuPID.setPID(driveStraightImu_Kp, driveStraightImu_Ki, driveStraightImu_Kd);
		imuPID.setAbsoluteTolerance(absTolerance_Imu_DriveStraight);
	}

	public void setImuForRotating() {
		imuPID.setPID(rotateImu_kP, rotateImu_kI, rotateImu_kD);
		imuPID.setAbsoluteTolerance(absTolerance_Imu_TurnTo);
	}

	public void strafeDrive(double xInput, double yInput) {

		double yOutput = 0, xOutput = 0; 

		if (Math.abs(xInput) > 0)
			dropCenterWheel(true);
		else 
			dropCenterWheel(false);
		
		
		double pidOut = dummyImuOutput.get(); 

		/* Code for strafe driving at any angle
		 * 
		 * Scales y input by tuning parameter to account for the varying speeds of for/rev and strafe wheels
		 * Divides by larger input to normalize one component
		 * Multiply by magnitude of controller input to set correct output values [0, 1]  
		 */

		double scaledYOutput = yInput / STRAFE_TUNING_PARAMETER; 

		yOutput = scaledYOutput / Math.max(Math.abs(xInput), Math.abs(scaledYOutput)) * 
				Math.sqrt(Math.pow(xInput, 2) + Math.pow(yInput, 2)); 

		xOutput = xInput / Math.max(Math.abs(xInput), Math.abs(scaledYOutput)) *
				Math.sqrt(Math.pow(xInput, 2) + Math.pow(yInput, 2)); 

		//		System.out.println("IMU PID enabled" + imuPID.isEnable());
		//
		//		System.out.println("xOut, yOut, pidOut \t" + xOutput + ", " + yOutput + ", " + pidOut);
		setMotors(yOutput + pidOut, yOutput - pidOut, xOutput);
		//		System.out.println(imu.getYaw() + " : " + imuPID.getSetpoint());
		//		System.out.println("current kP is: " + imuPID.getP());

	}

	public void setMotors(double left, double right, double center) {
		setLeftRight(left, right);
		setCenterWheel(center);
	}

	public void setImu(IMU imu) {
		this.imu = imu;

		dummyImuOutput = new DummyOutput();
		imuPID = new PIDController(rotateImu_kP, rotateImu_kI, rotateImu_kD, imu, dummyImuOutput);
		imuPID.setAbsoluteTolerance(absTolerance_Imu_DriveStraight);
		imuPID.setInputRange(-180, 180);
		imuPID.setContinuous(true);
	}

	/**
	 * Sends outputs values to the left and right side
	 * of the drive base.
	 *
	 * @param leftOutput
	 * @param rightOutput
	 */
	public void setLeftRight(double leftOutput, double rightOutput) {
		leftDrive.set(leftOutput * driveSpeed);
		rightDrive.set(-rightOutput * driveSpeed);
	}

	private void setCenterWheel(double val){
		centerDrive.set(val * driveSpeed);
	}

	/**
	 * Switch into high speed mode
	 */
	public void setHighSpeed() {
		driveSpeed = FAST_SPEED_RATING;
	}

	/**
	 * Switch into low speed mode
	 */
	public void setLowSpeed() {
		driveSpeed = SLOW_SPEED_RATING;
	}

	/**
	 * Switch to normal speed mode
	 */
	public void setNormalSpeed() {
		driveSpeed = NORMAL_SPEED_RATING;
	}

	/**
	 * Sets the drive to quick turn mode
	 * @param isQuickTurn
	 */
	public void setQuickTurn(boolean isQuickTurn) {
		this.isQuickTurn = isQuickTurn;
	}

	/**
	 * Sets maintainingHeading
	 * @param true or false
	 */
	public void setMaintainHeading(boolean b) {
		maintainingHeading = b; 
	}

	public void initPIDGyroRotate() {
		imuPID.setPID(rotateImu_kP, rotateImu_kI, rotateImu_kD);
		imuPID.setAbsoluteTolerance(absTolerance_Imu_TurnTo);
	}

	public double getAngle() {
		if (imu == null) 
			return 0;
		return imu.getYaw();
	}

	public void disableIMUPID() {
		imuPID.disable();
		maintainingHeading = false;
	}

	public void disableDriveStraightPID() {
		driveStraightPID.disable();
	}

	public void disableStrafePID() {
		strafePID.disable();
	}

	public void dropCenterWheel(boolean solValue) {
		suspension.set(solValue);
	}

	public void printLog() {
		//		System.out.print("Heading Error: " + Math.abs(imu.getYaw() - desiredHeading) + " ");
		//		System.out.println("Maintaining Heading: " + maintainingHeading);
		//		System.out.println("xInput: " + translateX + " yInput: " + translateY + 
		//				" xOutput:" + outputTX + " yOutput: " + outputTY);

		//System.out.println("Pos: " + imu.getYaw() + " Pos Target: " + imuPID.getSetpoint());

		//		System.out.println("Imu yaw: " + imu.getYaw());
		//    	System.out.println("Imu pitch: " + imu.getPitch());
	}

	/**
	 * Rotates the robot so that the IMU matches the angle
	 * @param angle
	 * @return
	 */
	public boolean rotateTo(double angle) { //may need to check for moving to fast when pid is on target
		if (imuPID == null) 
			throw new IllegalStateException("can't rotateTo when imu is null"); 


		if (!imuPID.isEnable()) {
			setImuForRotating();
			imuPID.setSetpoint(angle);
			imuPID.enable();
		}
		if (driveStraightPID != null && driveStraightPID.isEnable())
			driveStraightPID.disable();

		// Check to see if we're on target

		if (imuPID.onTarget()) {
			imuOnTargetCounter++;
			//			System.out.println("On target with count: " + imuOnTargetCounter);
		} else {
			imuOnTargetCounter = 0;
		}

		if (imuOnTargetCounter >= MINIMUM_IMU_ON_TARGET_ITERATIONS){
			setLeftRight(0, 0);
			imuPID.disable();
			//			System.out.println("Disabling PID with count: " + imuOnTargetCounter);
			return true;
		}

		//		System.out.println("IMU Error: " + imuPID.getError());

		double imuOutput = dummyImuOutput.get();
		setLeftRight(imuOutput, -imuOutput);
		return false;
	}
	/**
	 * Rotates angle degrees off of current angle.
	 * @param angle
	 * @return
	 */
	public boolean rotate(double angle){
		return rotateTo(imu.getYaw()+angle);
	}
	/**
	 * @param inches to drive forward
	 * @return true when robot has driven that many inches, false if not completed
	 */
	public boolean driveTo(double inches) {

		if(driveStraightPID == null)
			throw new IllegalStateException("Attempting to driveTo but no PID controller");

		if (!driveStraightPID.isEnable()) {
			dualEncoder.reset();
			driveStraightPID.enable();
			//			System.out.println("Enabling driveStraight PID in driveTo");
			driveStraightPID.setSetpoint(inches);
		}

		if(imuPID != null && !imuPID.isEnable()) {
			setImuForDrivingStraight();
			imuPID.setSetpoint(imu.getYaw());
			//			System.out.println("enabling IMU PID in driveTo");
			imuPID.enable();
		}

		double encoderOutput = dummyEncoderOutput.get();
		double leftOutput  = encoderOutput;
		double rightOutput = encoderOutput;

		double imuOutput = 0.0;
		if(imuPID != null)
			imuOutput = dummyImuOutput.get();


		//		System.out.println("leftEnc value: " + leftEnc.getDistance() + " rightEnc value: " + rightEnc.getDistance());
		//		System.out.println("dualEncoder: " + dualEncoder.getDistance());

		//		System.out.println("encoderPID output: " + encoderOutput + " imuPID output: " + imuOutput);
		//		System.out.println("error from enc PID " + driveStraightPID.getError());
		//		System.out.println("dual encoder rate: " + dualEncoder.getRate()); 
		//		System.out.println("signal sent: " + driveStraightPID.get());
		//		System.out.println("Kp from enc PID " + driveStraightPID.getP());

		//		just changed this sign
		setLeftRight(leftOutput + imuOutput, rightOutput - imuOutput);

		// Check to see if we're on target
		if (driveStraightPID.onTarget() && Math.abs(dualEncoder.getRate()) < lowEncRate) {
			//			System.out.println("Reached PID on target");
			setLeftRight(0.0, 0.0);
			driveStraightPID.disable();
			imuPID.disable();
			//			System.out.println("driveTo finished inside of driveTo");
			return true;
		}
		return false;
	}
	public IMU getIMU() {
		return imu;
	}

	public double getAbsoluteRate(){
		return dualEncoder.getAbsoluteRate();
	}

	public double getRate(){
		return dualEncoder.getRate();
	}
	//	public boolean strafeTo(double distance) {
	//		// TODO Auto-generated method stub
	//		return false;
	//	}

	public void setSlowStrafeOnlyMode(boolean b) {
		slowStrafeOnlyMode = b; 
	}
	
	public void setForcedNoStrafeMode(boolean b) {
		forcedNoStrafeMode = b; 
	}

	public void setOutputRange(double min, double max) {
		driveStraightPID.setOutputRange(min, max);
	}
}

