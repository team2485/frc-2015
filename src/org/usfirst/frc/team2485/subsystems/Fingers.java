package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Fingers {
	
	private VictorSP leftBelt, rightBelt;
	
	private Solenoid
		longFingerActuators,
		shortFingerActuators;
	
	private double intakeSpeed, reverseSpeed;
	private final double AXIS_DEADBAND = 0.125;
	
	private int fingerPosition;
	
	public static final int
		CLOSED		= 1,
		PARALLEL	= 2,
		OPEN		= 3;
	
	public Fingers(VictorSP leftBelt, VictorSP rightBelt,
			Solenoid longFingerActuators, Solenoid shortFingerActuators) {
		
		this.leftBelt				= leftBelt;
		this.rightBelt				= rightBelt;
		this.longFingerActuators	= longFingerActuators;
		this.shortFingerActuators	= shortFingerActuators;
		
		intakeSpeed  = 0.8;
		reverseSpeed = -0.8;
	}
	
	public Fingers(int leftBeltPort, int rightBeltPort,
			int longFingerActuatorsPort, int shortFingerActuatorsPort) {
		
		this(new VictorSP(leftBeltPort), new VictorSP(rightBeltPort),
				new Solenoid(longFingerActuatorsPort), new Solenoid(shortFingerActuatorsPort));
	}
	
	/*
	 * Handles tote by using the joystick to rotate it and pull it into clappers
	 */
	public void handleTote(float controllerY, float controllerZ) {
		
		controllerY = (float) handleThreshold(controllerY, AXIS_DEADBAND);
		controllerZ = (float) handleThreshold(controllerZ, AXIS_DEADBAND);
		
		if (controllerY == 0 && controllerZ == 0) 
			stopBelts();
	
		
		//first check for spinning belts in the other way
		if (controllerZ != 0) {
			if (controllerZ > 0) {
				setIntakeSpeed(1.0 * controllerZ);
				rotateToteRight();
			} else if (controllerZ < 0) {
				setIntakeSpeed(-1.0 * controllerZ);
				rotateToteLeft();
			}
		}
		//if we aren't trying to spin belts in opposite directions, move on here
		else if (controllerY != 0) {
			if (controllerY > 0) {
				setReverseSpeed(1.0 * controllerY);
				dualReverse();
			} else if (controllerY < 0) {
				setIntakeSpeed(1.0 * controllerY);
				dualIntake();
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
		rightBelt.set(-intakeSpeed);
	}

	/*
	 * Reverse a tote out of the clappers
	 */
	public void dualReverse() {
		leftBelt.set(-reverseSpeed);
		rightBelt.set(reverseSpeed);
	}

	/*
	 * Rotates tote left to align it for intake
	 * TODO: find out which way is left versus right
	 */
	public void rotateToteLeft() {
		leftBelt.set(-intakeSpeed);
		rightBelt.set(-intakeSpeed);
	}

	/*
	 * Rotates tote right to align it for intake
	 */
	public void rotateToteRight() {
		leftBelt.set(intakeSpeed);
		rightBelt.set(intakeSpeed);
	}
	
	public void stopBelts() {
		leftBelt.set(0);
		rightBelt.set(0);
	}

	/*
	 * Takes a parameter 1, 2, or 3 for the possible positions of the fingers.
	 * 3 is open
	 * 2 is flush or parallel
	 * 1 is closed
	 * Assumes that we will only be working with 3 positions, rather than the possible four
	 */
	public void setFingerPosition(int position) {
		
		fingerPosition = position;

		switch (position) {
			case 1: 
				longFingerActuators.set(true);
				shortFingerActuators.set(true);
				
				fingerPosition = 1;
				
				break;
			
			case 2:
				//TODO: check which actuator should be set to true
				longFingerActuators.set(false);
				shortFingerActuators.set(true);
				
				fingerPosition = 2;
				
				break;
			
			case 3:
				longFingerActuators.set(false);
				longFingerActuators.set(false);
				
				fingerPosition = 3;
				
				break;
			
			default:
				throw new IllegalArgumentException("Make the finger position 1, 2, or 3");
		}
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
	

}
