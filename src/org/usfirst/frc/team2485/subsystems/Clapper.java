package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Clapper {

	private VictorSP leftBelt, rightBelt;	

	private Solenoid
			leftFingerActuator0,
			leftFingerActuator1,
			rightFingerActuator0,
			rightFingerActuator1,
			clapperActuator, 
			intakeBeltsActuator;

	private double intakeSpeed, reverseSpeed;

	private boolean isOpen;

	public Clapper(VictorSP leftBelt, VictorSP rightBelt, 
			Solenoid leftFingerActuator0, Solenoid leftFingerActuator1,
			Solenoid rightFingerActuator0, Solenoid rightFingerActuator1,
			Solenoid clapperActuator, Solenoid intakeBeltsActuator) {
		
		this.leftBelt 				= leftBelt;
		this.rightBelt				= rightBelt;
		this.leftFingerActuator0	= leftFingerActuator0;
		this.leftFingerActuator1	= leftFingerActuator1;
		this.rightFingerActuator0	= rightFingerActuator0;
		this.rightFingerActuator1	= rightFingerActuator1;
		this.clapperActuator		= clapperActuator;
		this.intakeBeltsActuator	= intakeBeltsActuator;

		intakeSpeed  = 0.8;
		reverseSpeed = -0.8; 
	}

	public Clapper(int leftBelt, int rightBelt, int leftFingerActuator0,
			int leftFingerActuator1, int rightFingerActuator0, int rightFingerActuator1,
			int clapperActuator, int intakeBeltsActuator) {
		
		this(new VictorSP(leftBelt), new VictorSP(rightBelt), new Solenoid(leftFingerActuator0),
				new Solenoid(leftFingerActuator1), new Solenoid(rightFingerActuator0),
				new Solenoid(rightFingerActuator1), new Solenoid(clapperActuator), new Solenoid(intakeBeltsActuator));
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
		this.intakeSpeed = intakeSpeed;
	}

	public void setReverseSpeed(double reverseSpeed) {
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
				leftFingerActuator0.set(false);
				leftFingerActuator1.set(false);
				rightFingerActuator0.set(false);
				rightFingerActuator1.set(false);
				break;
			case 2:
				leftFingerActuator0.set(false);
				leftFingerActuator1.set(true);
				rightFingerActuator0.set(false);
				rightFingerActuator1.set(true);
				break;
			case 3:
				leftFingerActuator0.set(true);
				leftFingerActuator1.set(true);
				rightFingerActuator0.set(true);
				rightFingerActuator1.set(true);
				break;
			default:
				throw new IllegalArgumentException("Make the position 1, 2, or 3");
		
		
		}
		
			
	}

	public void openClapper() {
		clapperActuator.set(true);
	}

	public void closeClapper() {
		clapperActuator.set(false);
	}



	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
}