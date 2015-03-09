package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.util.ThresholdHandler;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Ben Clark
 */
public class Fingers {

	private VictorSP leftBelt, rightBelt;

	private Solenoid
	longFingerActuators,
	shortFingerActuators;

	private final double AXIS_DEADBAND = 0.25;

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

	/**
	 * Handles tote by using the joystick to rotate it and pull it into clappers
	 */
	public void handleTote(float controllerY, float controllerZ) {

		controllerY = (float) ThresholdHandler.handleThreshold(controllerY, AXIS_DEADBAND);
		controllerZ = (float) ThresholdHandler.handleThreshold(controllerZ, AXIS_DEADBAND);

		//		System.out.println("controllerY: " + controllerY + "controllerZ: " + controllerZ);

		if (Math.abs(controllerZ) > 0.05) {
			if (controllerZ > 0) 	
				rotateToteRight(controllerZ);
			else if (controllerZ < 0) 
				rotateToteLeft(controllerZ);
		} else if (Math.abs(controllerY) > 0.05) {
			if (controllerY > 0) 
				dualIntake(controllerY);
			else if (controllerY < 0) 
				dualReverse(controllerY);
		} else 
			stopBelts();
	}

	/**
	 * Intakes a tote where both belts run in the same direction
	 */
	public void dualIntake(double speed) {
		leftBelt.set(Math.abs(speed));
		rightBelt.set(-Math.abs(speed));
	}

	/**
	 * Reverse a tote out of the clappers
	 */
	public void dualReverse(double speed) {
		leftBelt.set(-Math.abs(speed));
		rightBelt.set(Math.abs(speed));
	}

	/**
	 * Rotates tote left to align it for intake
	 */
	public void rotateToteLeft(double speed) {
		leftBelt.set(Math.abs(speed));
		rightBelt.set(Math.abs(speed));
	}

	/**
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

	/**
	 * @param Fingers Position (Closed, parallell or open)
	 * ex:
	 * <code>Robot.fingers.setFingerPosition(Fingers.CLOSED); </code>
	 * 
	 * Only set fingers to those 3 positions rather than the mechincally possible 4. 
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
				longFingerActuators.set(false);
				shortFingerActuators.set(true);
	
				fingerPosition = PARALLEL;
	
				break;
	
			case OPEN:
				longFingerActuators.set(false);
				shortFingerActuators.set(false);
	
				fingerPosition = OPEN;
	
				break;
	
			default:
				throw new IllegalArgumentException("Make the finger position CLOSED, PARALLEL, or OPEN");
		}
	}

}