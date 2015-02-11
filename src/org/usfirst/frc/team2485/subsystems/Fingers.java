package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Fingers {
	
	private VictorSP leftBelt, rightBelt;
	
	private Solenoid
		longFingerActuators,
		shortFingerActuators;
	
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
		
		if (Math.abs(controllerZ) > 0.05) {
			if (controllerZ > 0) {
				rotateToteRight(controllerZ);
			} else if (controllerZ < 0) {
				rotateToteLeft(controllerZ);
			}
		}
		else if (Math.abs(controllerY) > 0.05) {
			if (controllerY > 0) {
				dualReverse(controllerY);
			} else if (controllerY < 0) {
				dualIntake(controllerY);
			}
		}	
		else {
			stopBelts();
		}
	}
	
	/*
	 * Intakes a tote where both belts run in the same direction
	 */
	public void dualIntake(double speed) {
		leftBelt.set(Math.abs(speed));
		rightBelt.set(-Math.abs(speed));
	}

	/*
	 * Reverse a tote out of the clappers
	 */
	public void dualReverse(double speed) {
		leftBelt.set(-Math.abs(speed));
		rightBelt.set(Math.abs(speed));
	}

	/*
	 * Rotates tote left to align it for intake
	 * TODO: find out which way is left versus right
	 */
	public void rotateToteLeft(double speed) {
		leftBelt.set(Math.abs(speed));
		rightBelt.set(Math.abs(speed));
	}

	/*
	 * Rotates tote right to align it for intake
	 */
	public void rotateToteRight(double speed) {
		leftBelt.set(-Math.abs(speed));
		rightBelt.set(-Math.abs(speed));
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
			case CLOSED: 
				longFingerActuators.set(true);
				shortFingerActuators.set(true);
				
				fingerPosition = CLOSED;
				
				break;
			
			case PARALLEL:
				//TODO: check which actuator should be set to true
				longFingerActuators.set(false);
				shortFingerActuators.set(true);
				
				fingerPosition = PARALLEL;
				
				break;
			
			case OPEN:
				longFingerActuators.set(false);
				shortFingerActuators.set(false);
				System.out.println("detected open in Fingers");
				fingerPosition = OPEN;
				
				break;
			
			default:
				throw new IllegalArgumentException(
						"Make the finger position CLOSED, PARALLEL, or OPEN");
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