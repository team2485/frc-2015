package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Fingers {
	
	private VictorSP leftBelt, rightBelt;
	
	private Solenoid
		leftFingerActuator1,
		leftFingerActuator2,
		rightFingerActuator1,
		rightFingerActuator2;
	
	private double intakeSpeed, reverseSpeed;
	
	private int fingerPosition;
	
	public Fingers(VictorSP leftBelt, VictorSP rightBelt,
			Solenoid leftFingerActuator1, Solenoid leftFingerActuator2,
			Solenoid rightFingerActuator1, Solenoid rightFingerActuator2) {
		
		this.leftBelt				= leftBelt;
		this.rightBelt				= rightBelt;
		this.leftFingerActuator1 	= leftFingerActuator1;
		this.leftFingerActuator2	= leftFingerActuator2;
		this.rightFingerActuator1	= rightFingerActuator1;
		this.rightFingerActuator2	= rightFingerActuator2;
		
		intakeSpeed  = 0.8;
		reverseSpeed = -0.8;
	}
	
	public Fingers(int leftBeltPort, int rightBeltPort,
			int leftFingerActuator1Port, int leftFingerActuator2Port,
			int rightFingerActuator1Port, int rightFingerActuator2Port) {
		
		this(new VictorSP(leftBeltPort), new VictorSP(rightBeltPort),
				new Solenoid(leftFingerActuator1Port), new Solenoid(leftFingerActuator2Port),
				new Solenoid(rightFingerActuator1Port), new Solenoid(rightFingerActuator2Port));
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
		
		fingerPosition = position;

		switch (position) {
			case 1: 
				leftFingerActuator1.set(false);
				leftFingerActuator2.set(false);
				rightFingerActuator1.set(false);
				rightFingerActuator2.set(false);
				
				fingerPosition = 1;
				
				break;
			
			case 2:
				leftFingerActuator1.set(false);
				leftFingerActuator2.set(true);
				rightFingerActuator1.set(false);
				rightFingerActuator2.set(true);
				
				fingerPosition = 2;
				
				break;
			
			case 3:
				leftFingerActuator1.set(true);
				leftFingerActuator2.set(true);
				rightFingerActuator1.set(true);
				rightFingerActuator2.set(true);
				
				fingerPosition = 3;
				
				break;
			
			default:
				throw new IllegalArgumentException("Make the finger position 1, 2, or 3");
		}
	}
	

}
