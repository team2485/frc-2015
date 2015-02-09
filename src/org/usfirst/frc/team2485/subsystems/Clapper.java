package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Clapper {

	private VictorSP
	leftBelt, 
	rightBelt, 
	clapperLifter1, 
	clapperLifter2;	

	private Solenoid
	leftFingerActuator1,
	leftFingerActuator2,
	rightFingerActuator1,
	rightFingerActuator2,
	clapperActuator1,
	clapperActuator2;

	private PIDController clapperPID;
	private AnalogPotentiometer pot;
	private static final int POT_OFFSET = 0;
	private boolean open;

	private double intakeSpeed, reverseSpeed;

	public Clapper(VictorSP leftBelt, VictorSP rightBelt,
			VictorSP clapperLifter1, VictorSP clapperLifter2,
			Solenoid leftFingerActuator1, Solenoid leftFingerActuator2,
			Solenoid rightFingerActuator1, Solenoid rightFingerActuator2,
			Solenoid clapperActuator1,Solenoid clapperActuator2) {

		this.leftBelt 				= leftBelt;
		this.rightBelt				= rightBelt;
		this.clapperLifter1			= clapperLifter1;
		this.clapperLifter2			= clapperLifter2;
		this.leftFingerActuator1	= leftFingerActuator1;
		this.leftFingerActuator2	= leftFingerActuator2;
		this.rightFingerActuator1	= rightFingerActuator1;
		this.rightFingerActuator2	= rightFingerActuator2;
		this.clapperActuator1		= clapperActuator1;
		this.clapperActuator2		= clapperActuator2;

		intakeSpeed  = 0.8;
		reverseSpeed = -0.8;

	}

	public Clapper(int leftBelt, int rightBelt, int clapperLifter1,
			int clapperLifter2, int leftFingerActuator1,
			int leftFingerActuator2, int rightFingerActuator1,
			int rightFingerActuator2, int clapperActuator1,
			int clapperActuator2) {

		this(new VictorSP(leftBelt), new VictorSP(rightBelt), new VictorSP(clapperLifter1),
				new VictorSP(clapperLifter2), new Solenoid(leftFingerActuator1),
				new Solenoid(leftFingerActuator2), new Solenoid(rightFingerActuator1),
				new Solenoid(rightFingerActuator2), new Solenoid(clapperActuator1),
				new Solenoid(clapperActuator2));
	}

	/*
	 * Handles tote by using the joystick to rotate it and pull it into clappers
	 */
	public void handleTote(double controllerY, double controllerZ) {
		if (controllerY != 0) {
			if (controllerY > 0) {
				setReverseSpeed(1.0 * controllerY);
				dualReverse();
			} else if (controllerY < 0) {
				setIntakeSpeed(1.0 * controllerY);
				dualIntake();
			}
		}	
		if (controllerZ != 0) {
			if (controllerZ > 0) {
				setIntakeSpeed(1.0 * controllerZ);
				rotateToteRight();
			} else if (controllerZ < 0) {
				setIntakeSpeed(-1.0 * controllerZ);
				rotateToteLeft();
			}
		}
	}

	public void setIntakeSpeed(double intakeSpeed) {
		if (intakeSpeed > 1)
			intakeSpeed = 1; 
		else if (intakeSpeed < -1)
			intakeSpeed = -1; 
		else 
			this.intakeSpeed = intakeSpeed;
	}

	public void setReverseSpeed(double reverseSpeed) {
		if (reverseSpeed > 1)
			reverseSpeed = 1;
		else if (reverseSpeed < -1)
			reverseSpeed = -1; 
		else
			this.reverseSpeed = reverseSpeed;
	}

	/*
	 * Intakes a tote where both belts run in the same direction
	 */
	public void dualIntake() {
		leftBelt.set(intakeSpeed);
		rightBelt.set(intakeSpeed);
	}

	/*
	 * Reverse a tote out of the clappers
	 */
	public void dualReverse() {
		leftBelt.set(reverseSpeed);
		rightBelt.set(reverseSpeed);
	}

	/*
	 * Rotates tote left to align it for intake
	 */
	public void rotateToteLeft() {
		leftBelt.set(intakeSpeed);
		rightBelt.set(-intakeSpeed);
	}

	/*
	 * Rotates tote right to align it for intake
	 */
	public void rotateToteRight() {
		leftBelt.set(-intakeSpeed);
		rightBelt.set(intakeSpeed);
	}

	/*
	 * Takes a parameter 1, 2, or 3 for the possible positions of the fingers.
	 * 1 is closed
	 * 2 is flush or parallel
	 * 3 is open
	 * Assumes that we will only be working with 3 positions, rather than the possible four
	 */
	public void setFingerPosition(int position) {

		switch (position) {
		case 1: 
			leftFingerActuator1.set(false);
			leftFingerActuator2.set(false);
			rightFingerActuator1.set(false);
			rightFingerActuator2.set(false);
			break;
		case 2:
			leftFingerActuator1.set(false);
			leftFingerActuator2.set(true);
			rightFingerActuator1.set(false);
			rightFingerActuator2.set(true);
			break;
		case 3:
			leftFingerActuator1.set(true);
			leftFingerActuator2.set(true);
			rightFingerActuator1.set(true);
			rightFingerActuator2.set(true);
			break;
		default:
			throw new IllegalArgumentException("Make the position 1, 2, or 3");


		}


	}

	public void openClapper() {
		clapperActuator1.set(true);
		clapperActuator2.set(true);
		open = true;
	}

	public void closeClapper() {
		clapperActuator1.set(false);
		clapperActuator2.set(false);
		open = false;
	}



	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
}