package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.team2485.util.DummyOutput;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class DriveTrain {
	private VictorSP leftDrive, leftDrive2, rightDrive, rightDrive2, centerDrive, test1, test2, test3, test4;
	private Solenoid suspension;
	private Encoder centerEnc, leftEnc, rightEnc;
	private DummyOutput dummyImuOutput;
	private PIDController imuPID;
	public static double
	kP_G_Rotate = 0.028,
	kI_G_Rotate = 0.0,
	kD_G_Rotate = 0.0;

	private final double 
	NORMAL_SPEED_RATING = 1.0,
	FAST_SPEED_RATING = 1.0,
	SLOW_SPEED_RATING = 0.6;

	private double driveSpeed = NORMAL_SPEED_RATING;

	private double oldWheel = 0.0;
	private double quickStopAccumulator = 0.0;
	private final double TRANSLATE_Y_DEADBAND = 0.1;
	private final double TRANSLATE_X_DEADBAND = 0.1;
	private final double ROTATION_DEADBAND = 0.1;
	private final double STRAFE_TUNING_PARAMETER = 3;

	private final double SENSITIVITY_HIGH = 0.85;
	//	private final double SENSITIVITY_LOW = 0.75;
	private final double MUCH_TOO_HIGH_SENSITIVITY = 1.7;
	private boolean isQuickTurn = false;

	//driving straight
	private double desiredHeading = 0.0; 
	private boolean maintainingHeading = true; //use for auto and while !rotating  
	private final double AbsTolerance_Imu_TurnTo = 3.0;
	private final double AbsTolerance_Imu_DriveStraight = 2.0;

	private IMU imu;

	private String logString = ""; 

	private double translateX = 0, translateY = 0, outputTX = 0, outputTY = 0;

	public DriveTrain(VictorSP leftDrive, VictorSP rightDrive, VictorSP centerDrive, IMU imu, Encoder centerEnc, Encoder leftEnc, Encoder rightEnc) {
		this.leftDrive      = leftDrive;
		this.rightDrive     = rightDrive;
		this.centerDrive	= centerDrive;
		this.imu            = imu;
		this.centerEnc		= centerEnc;
		this.leftEnc		= leftEnc;
		this.rightEnc		= rightEnc;

		if (imu != null) {
			dummyImuOutput = new DummyOutput();
			imuPID = new PIDController(kP_G_Rotate, kI_G_Rotate, kD_G_Rotate, imu, dummyImuOutput);
			imuPID.setAbsoluteTolerance(AbsTolerance_Imu_DriveStraight);
		}
	}

	public DriveTrain(VictorSP leftDrive, VictorSP leftDrive2, VictorSP rightDrive, VictorSP rightDrive2, VictorSP centerDrive, Solenoid suspension) {
		this.leftDrive      = leftDrive;
		this.leftDrive2     = leftDrive2;
		this.rightDrive     = rightDrive;
		this.rightDrive2    = rightDrive2;
		this.centerDrive	= centerDrive;
		this.suspension 	= suspension;
	}

	public DriveTrain(int leftDrivePort, int leftDrivePort2, int rightDrivePort, int rightDrivePort2, int centerDrivePort, int suspensionPort) {
		this(new VictorSP(leftDrivePort), new VictorSP(leftDrivePort2), new VictorSP(rightDrivePort), new VictorSP(rightDrivePort2), new VictorSP(centerDrivePort), new Solenoid(suspensionPort));
	}

	public void warlordDrive(double translateX, double translateY, double rotation) {

		translateX = -handleThreshold(translateX, TRANSLATE_X_DEADBAND);
		translateY = handleThreshold(translateY, TRANSLATE_Y_DEADBAND);
		rotation = handleThreshold(rotation, ROTATION_DEADBAND);

		printLog(); 

		if(rotation != 0) {
			if (maintainingHeading) {
				maintainingHeading = false; 
				//				imuPID.disable(); 
			}
			rotationalDrive(translateY, rotation);
		}

		else {
			if (!maintainingHeading) {
				maintainingHeading = true; 
				//				desiredHeading = imu.getYaw(); 
				//				
				//				imuPID.enable();
				//				imuPID.setSetpoint(desiredHeading);
			}
			strafeDrive(translateX, translateY);
		}
	}

	public void rotationalDrive(double throttle, double wheel) {
		//		suspension.set(false);

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
		sensitivity = SENSITIVITY_HIGH;

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

	public void strafeDrive(double xInput, double yInput) {
		
		if (yInput == 0 && xInput == 0) {
			setLeftRight(0,0);
			setCenterWheel(0);
			return;
		}

		double yOutput = 0, xOutput = 0; 

		//		suspension.set(true);
		//		double pidOut = dummyImuOutput.get(); 

		this.translateX = xInput; 
		this.translateY = yInput; 
				
		//		if(xInput == 0) {
		//			xOutput = 0;
		//		}
		//		
		//		else{
		//		if (xInput != 0)
		//			yOutput = yInput / STRAFE_TUNING_PARAMETER / Math.max(Math.abs(xInput), Math.abs(yInput)) *
		//			Math.sqrt(Math.pow(xInput, 2) + Math.pow(yInput, 2)); 
		//		else 
		//			yOutput = yInput; 


		/*
		 * Code for strafe driving at any angle
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
		//		}
		// check the signs of this
		//		setMotors(yOutput + pidOut, yOutput - pidOut, xOutput);
		setMotors(yOutput, yOutput, xOutput);

	}

	public void setMotors(double left, double right, double center) {
		setLeftRight(left, right);
		setCenterWheel(center);
	}

	// <editor-fold defaultstate="collapsed" desc="General Methods">

	public void setImu(IMU imu) {
		this.imu = imu;

		dummyImuOutput = new DummyOutput();
		imuPID = new PIDController(kP_G_Rotate, kI_G_Rotate, kD_G_Rotate, imu, dummyImuOutput);
		imuPID.setAbsoluteTolerance(AbsTolerance_Imu_DriveStraight);
	}

	/**
	 * Sends outputs values to the left and right side
	 * of the drive base.
	 *
	 * @param leftOutput
	 * @param rightOutput
	 */
	private void setLeftRight(double leftOutput, double rightOutput) {
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
		return (Math.abs(val) > Math.abs(threshold)) ? val : 0.0;
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
		imuPID.setPID(kP_G_Rotate, kI_G_Rotate, kD_G_Rotate);
		imuPID.setAbsoluteTolerance(AbsTolerance_Imu_TurnTo);
	}

	public double getAngle() {
		if (imu == null) 
			return 0;
		return imu.getYaw();
	}

	public void disableIMUPID() {
		imuPID.disable();
	}

	public void setSolenoid(boolean solValue) {
		suspension.set(solValue);
	}

	public void printLog() {
		//    	System.out.print("Heading Error: " + Math.abs(imu.getYaw() - desiredHeading) + " ");
		//    	System.out.println("Maintaining Heading: " + maintainingHeading);
		System.out.println("xInput: " + translateX + " yInput: " + translateY + 
				" xOutput:" + outputTX + " yOutput: " + outputTY);

	}
}

