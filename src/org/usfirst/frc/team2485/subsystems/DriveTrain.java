
package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.util.DualEncoder;
import org.usfirst.frc.team2485.util.DummyOutput;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class DriveTrain {

	private VictorSP leftDrive, leftDrive2, rightDrive, rightDrive2, centerDrive, test1, test2, test3, test4;
	private Solenoid suspension;
	private Encoder centerEnc, leftEnc, rightEnc;

	private final double 
	NORMAL_SPEED_RATING = 0.75,
	FAST_SPEED_RATING = 1.0,
	SLOW_SPEED_RATING = 0.5;

	private double driveSpeed = NORMAL_SPEED_RATING;

	private double oldWheel = 0.0;
	private double quickStopAccumulator = 0.0;
	private final double TRANSLATE_Y_DEADBAND = 0.125;
	private final double TRANSLATE_X_DEADBAND = 0.125;
	private final double ROTATION_DEADBAND = 0.2;
	private double STRAFE_TUNING_PARAMETER = 3;

	private final double SENSITIVITY_HIGH = 0.85;
	private final double SENSITIVITY_LOW = 0.55;
	private final double MUCH_TOO_HIGH_SENSITIVITY = 1.7;
	private boolean isQuickTurn = false;
	

	//PID 
	public double desiredHeading = 0.0; 
	public boolean maintainingHeading = true; //use for auto and while !rotating  

	public IMU imu;

	private double translateX = 0, translateY = 0, outputTX = 0, outputTY = 0;

	private boolean buttonClicked = false;

	private DualEncoder dualEncoder;

	private PIDController driveStraightPID;
	private PIDController strafePID;
	public PIDController imuPID;

	private DummyOutput dummyEncoderOutput;
	private DummyOutput dummyImuOutput;

	private double lowEncRate = 5;
	private int imuOnTargetCounter = 0;
	private final int MINIMUM_IMU_ON_TARGET_ITERATIONS = 6;
	
	private final double
		absTolerance_Imu_TurnTo = 1.0,
		absTolerance_Imu_DriveStraight = 2.0,
		absTolerance_Enc_DriveStraight = 3.0,
		absTolerance_Enc_Strafe = 3.0; 

	public static double 
		driveStraightEncoder_Kp = 0.06, 
		driveStraightEnvoder_Ki = 0.0, 
		driveStraightEncoder_Kd = 0.0;

	public static double 
		strafeEncoder_Kp = 0.005,
		strafeEncoder_Ki = 0.0,
		strafeEncoder_Kd = 0.0;
	
	public static double
		driveStraightImu_Kp = 0.05, //0.05 - for floor work, 0.07 for bump (tentatively)
		driveStraightImu_Ki = 0.0,
		driveStraightImu_Kd = 0.0; 

	public static double
		rotateImu_kP = 0.045,
		rotateImu_kI = 0.0,
		rotateImu_kD = 0.005;

//	public DriveTrain(VictorSP leftDrive, VictorSP leftDrive2, VictorSP rightDrive, 
//			VictorSP rightDrive2, VictorSP centerDrive, Solenoid suspension, IMU imu, Encoder leftEnc, 
//			Encoder rightEnc, Encoder centerEnc) {
//
//		this.leftDrive      = leftDrive;
//		this.leftDrive2		= leftDrive2; 
//		this.rightDrive     = rightDrive;
//		this.rightDrive2	= rightDrive2; 
//		this.centerDrive	= centerDrive;
//		this.suspension 	= suspension;
//		this.imu            = imu;
//		this.centerEnc		= centerEnc;
//		this.leftEnc		= leftEnc;
//		this.rightEnc		= rightEnc;
//
//		if (this.imu != null) {
//			setImu(this.imu);
//		}
//
//		if(leftEnc != null && rightEnc != null) {	
//			dummyEncoderOutput = new DummyOutput();
//			dualEncoder = new DualEncoder(leftEnc, rightEnc);
//			driveStraightPID = new PIDController(driveStraightEncoder_Kp, driveStraightEnvoder_Ki, driveStraightEncoder_Kd, dualEncoder, dummyEncoderOutput);
//			driveStraightPID.setAbsoluteTolerance(absTolerance_Enc_DriveStraight);
//		}
//
//		if(centerEnc != null) {
//			strafePID = new PIDController(strafeEncoder_Kp, strafeEncoder_Ki, strafeEncoder_Kd, centerEnc, centerDrive);
//			strafePID.setAbsoluteTolerance(absTolerance_Enc_Strafe);
//		}
//	}
	
	public DriveTrain(VictorSP leftDrive, VictorSP leftDrive2, VictorSP rightDrive, 
			VictorSP rightDrive2, IMU imu, Encoder leftEnc, 
			Encoder rightEnc) {

		this.leftDrive      = leftDrive;
		this.leftDrive2		= leftDrive2; 
		this.rightDrive     = rightDrive;
		this.rightDrive2	= rightDrive2; 
		this.imu            = imu;
		this.leftEnc		= leftEnc;
		this.rightEnc		= rightEnc;

		if (this.imu != null) {
			setImu(this.imu);
		}

//		if(leftEnc != null && rightEnc != null) {	
//			dummyEncoderOutput = new DummyOutput();
//			dualEncoder = new DualEncoder(leftEnc, rightEnc);
//			driveStraightPID = new PIDController(driveStraightEncoder_Kp, driveStraightEnvoder_Ki, driveStraightEncoder_Kd, dualEncoder, dummyEncoderOutput);
//			driveStraightPID.setAbsoluteTolerance(absTolerance_Enc_DriveStraight);
//		}
//
//		if(centerEnc != null) {
//			strafePID = new PIDController(strafeEncoder_Kp, strafeEncoder_Ki, strafeEncoder_Kd, centerEnc, centerDrive);
//			strafePID.setAbsoluteTolerance(absTolerance_Enc_Strafe);
//		}
	}
	/*
	public DriveTrain(VictorSP leftDrive, VictorSP leftDrive2, VictorSP rightDrive, 
			VictorSP rightDrive2, VictorSP centerDrive, Solenoid suspension, IMUAdvanced imu) {

		this(leftDrive, leftDrive2, rightDrive, rightDrive2, centerDrive, suspension, imu,
				null, null, null);
	}

	public DriveTrain(int leftDrivePort, int leftDrivePort2, int rightDrivePort, int rightDrivePort2, 
			int centerDrivePort, int suspensionPort, SerialPort imuPort) {

		this(new VictorSP(leftDrivePort), new VictorSP(leftDrivePort2), new VictorSP(rightDrivePort),
				new VictorSP(rightDrivePort2), new VictorSP(centerDrivePort), new Solenoid(suspensionPort),
				new IMUAdvanced(new SerialPort(57600, SerialPort.Port.kUSB)));
	}
	 */
	public void warlordDrive(double translateX, double translateY, double rotation) {

		translateX = -handleThreshold(translateX, TRANSLATE_X_DEADBAND);
		translateY = -handleThreshold(translateY, TRANSLATE_Y_DEADBAND);
		rotation = handleThreshold(rotation, ROTATION_DEADBAND);

		System.out.println("x, y \t\t" + translateX + ",\t" + translateY);

		printLog();    

		if(rotation != 0) {
			if (maintainingHeading) {
				maintainingHeading = false; 
				imuPID.disable(); 
			}
			rotationalDrive(translateY, rotation);
		}

		else {
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

	public void rotationalDrive(double throttle, double wheel) {
		//suspension.set(true);

		//		double maxDelta = 0.05;
		//		if(Math.abs(wheel - oldWheel) > maxDelta)
		//			if(wheel > oldWheel)
		//				wheel = oldWheel + maxDelta;
		//			else
		//				wheel = oldWheel - maxDelta;
		//		if(wheel > 1)
		//			wheel = 1;
		//		else if (wheel < -1)
		//			wheel = -1;
		//		oldWheel = wheel;
		//		
		//		setLeftRight(wheel, -wheel);   
		//		return;   

		double negInertia = wheel - oldWheel;
		oldWheel = wheel;

		double wheelNonLinearity = 0.5;

		//this was the low gear code, since we only have one gear
		wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) /
				Math.sin(Math.PI / 2.0 * wheelNonLinearity);
		wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) /
				Math.sin(Math.PI / 2.0 * wheelNonLinearity);
		wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) /
				Math.sin(Math.PI / 2.0 * wheelNonLinearity);

		double sensitivity = MUCH_TOO_HIGH_SENSITIVITY;
		double leftPwm, rightPwm, overPower;

		double angularPower;
		double linearPower;

		// Negative inertia!
		double negInertiaAccumulator = 0.0;
		double negInertiaScalar;

		negInertiaScalar = 5.0;
		sensitivity = SENSITIVITY_LOW;

		double negInertiaPower = negInertia * negInertiaScalar;
		negInertiaAccumulator += negInertiaPower;

		wheel = wheel + negInertiaAccumulator;
		linearPower = throttle;

		if (isQuickTurn) {
			if (Math.abs(linearPower) < 0.2) {
				double alpha = 0.1;
				wheel = wheel > 1 ? 1.0 : wheel;
				quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha *
						wheel * 0.5;
			}
			overPower = 1.0;            
			angularPower = wheel;
		} else {
			overPower = 0.0;
			angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;
			if (quickStopAccumulator > 1) {
				quickStopAccumulator -= 1;
			} else if (quickStopAccumulator < -1) {
				quickStopAccumulator += 1;
			} else {
				quickStopAccumulator = 0.0;
			}
		}

		rightPwm = leftPwm = linearPower;

		leftPwm  += angularPower;
		rightPwm -= angularPower;

		//lower sensitivity -- 1/31/15 debugging 
		leftPwm  *= Math.abs(leftPwm); 
		rightPwm *= Math.abs(rightPwm); 

		if (leftPwm > 1.0) {
			rightPwm -= overPower * (leftPwm - 1.0);
			leftPwm = 1.0;
		} else if (rightPwm > 1.0) {
			leftPwm -= overPower * (rightPwm - 1.0);
			rightPwm = 1.0;
		} else if (leftPwm < -1.0) {
			rightPwm += overPower * (-1.0 - leftPwm);
			leftPwm = -1.0;
		} else if (rightPwm < -1.0) {
			leftPwm += overPower * (-1.0 - rightPwm);
			rightPwm = -1.0;
		}

		setLeftRight(leftPwm, rightPwm);
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

		if (Math.abs(yInput) <= .1 && Math.abs(xInput) <= .1) {
			setLeftRight(0,0);
			setCenterWheel(0);
			return;
		}

		double yOutput = 0, xOutput = 0; 

		//suspension.set(true);
		double pidOut = dummyImuOutput.get(); 

		this.translateX = xInput; 
		this.translateY = yInput; 





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

		this.outputTX = xOutput; 
		this.outputTY = yOutput; 
		System.out.println("IMU PID enabled" + imuPID.isEnable());

		System.out.println("xOut, yOut, pidOut \t" + xOutput + ", " + yOutput + ", " + pidOut);
		//		}
		// check the signs of this
		setMotors(yOutput + pidOut, yOutput - pidOut, xOutput);
		//		setMotors(yOutput, yOutput, xOutput);
		System.out.println(imu.getYaw() + " : " + imuPID.getSetpoint());
		System.out.println("current kP is: " + imuPID.getP());

	}

	public void setMotors(double left, double right, double center) {
		setLeftRight(left, right);
		setCenterWheel(center);
	}

	// <editor-fold defaultstate="collapsed" desc="General Methods">

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
		leftDrive2.set(leftOutput * driveSpeed);
		rightDrive.set(-rightOutput * driveSpeed);
		rightDrive2.set(-rightOutput * driveSpeed);
	}

	private void setCenterWheel(double val){
		centerDrive.set(val * driveSpeed);
	}

	/**
	 * Thresholds values
	 *
	 * @param val
	 * @param deadband
	 * @return
	 */
	private double handleThreshold(double val, double threshold) {

		double returnValue = (Math.abs(val) > Math.abs(threshold)) ? (val/Math.abs(val)*(Math.abs(val)-threshold)/(1-threshold)) : 0.0;
		//System.out.println(val + " : " + returnValue);
		return returnValue;

		//return (Math.abs(val) > Math.abs(threshold)) ? val : 0.0;
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

	public void setSolenoid(boolean solValue) {
		suspension.set(solValue);
	}

	public void printLog() {
		//		System.out.print("Heading Error: " + Math.abs(imu.getYaw() - desiredHeading) + " ");
		//		System.out.println("Maintaining Heading: " + maintainingHeading);
		//		System.out.println("xInput: " + translateX + " yInput: " + translateY + 
		//				" xOutput:" + outputTX + " yOutput: " + outputTY);

		//System.out.println("Pos: " + imu.getYaw() + " Pos Target: " + imuPID.getSetpoint());
		
		System.out.println("Imu yaw: " + imu.getYaw());
    	System.out.println("Imu pitch: " + imu.getPitch());
	}
	public void tuneStrafeParam(double d) {
		if (buttonClicked) {
			//			kP_G_Rotate += d; 
			STRAFE_TUNING_PARAMETER += Math.abs(d)/d; 
			buttonClicked = false; 
			System.out.println("new strafe param: " + STRAFE_TUNING_PARAMETER);
		}
	}
	public void resetButtonClicked() {
		buttonClicked = true; 
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
			System.out.println("On target with count: " + imuOnTargetCounter);
		} else {
			imuOnTargetCounter = 0;
		}
		
		if (imuOnTargetCounter >= MINIMUM_IMU_ON_TARGET_ITERATIONS){
			setLeftRight(0, 0);
			imuPID.disable();
			System.out.println("Disabling PID with count: " + imuOnTargetCounter);
			return true;
		}

		System.out.println("IMUval: " + imu.getYaw() + " IMUdsetPoint: " + imuPID.getSetpoint() + " IMU Error: " + imuPID.getError() + " IMU enabled: " + imuPID.isEnable());
		
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
	public boolean driveTo(double inches) {

		if(driveStraightPID == null)
			throw new IllegalStateException("Attempting to driveTo but no PID controller");

		if (!driveStraightPID.isEnable()) {
			dualEncoder.reset();
			driveStraightPID.enable();
			driveStraightPID.setSetpoint(inches);
		}

		if(imuPID != null && !imuPID.isEnable()) {
			setImuForDrivingStraight();
			imuPID.setSetpoint(imu.getYaw());
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
//
//		System.out.println("encoderPID output: " + encoderOutput + " imuPID output: " + imuOutput);
//		System.out.println("error from enc PID " + driveStraightPID.getError() + " from imu PID " + imuPID.getError());
//		System.out.println("Kp from enc PID " + driveStraightPID.getP());

//		just changed this sign
		setLeftRight(leftOutput + imuOutput, rightOutput - imuOutput);

		// Check to see if we're on target
		if (driveStraightPID.onTarget() && Math.abs(dualEncoder.getRate()) < lowEncRate) {
			setLeftRight(0.0, 0.0);
			driveStraightPID.disable();
			imuPID.disable();
			return true;
		}
		return false;
	}
	public IMU getIMU() {
		return imu;
	}
}

