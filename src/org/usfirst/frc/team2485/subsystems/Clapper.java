package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Clapper {
	
	private VictorSP leftBelt, rightBelt;	
	private Solenoid fingerActuator, clapperActuator, intakeBeltsActuator;
	public double intakeSpeed, reverseSpeed;
	
	public Clapper(VictorSP leftBelt, VictorSP rightBelt, 
				   Solenoid fingerActuator, Solenoid clapperActuator, Solenoid intakeBeltsActuator) {
		this.leftBelt 				= leftBelt;
		this.rightBelt				= rightBelt;
		this.fingerActuator			= fingerActuator;
		this.clapperActuator		= clapperActuator;
		this.intakeBeltsActuator	= intakeBeltsActuator;
		
		intakeSpeed  = 0.8;
		reverseSpeed = -0.8; 
	}
	
	public Clapper(int leftBelt, int rightBelt, int fingerActuator, int clapperActuator, int intakeBeltsActuator) {
		this(new VictorSP(leftBelt), new VictorSP(rightBelt), 
			 new Solenoid(fingerActuator), new Solenoid(clapperActuator), new Solenoid(intakeBeltsActuator));
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
	public void reverseIntake() {
		leftBelt.set(reverseSpeed);
		rightBelt.set(reverseSpeed);
	}
	
	/*
	 * Runs one intake belt in an opposite direction to align a tote for intake
	 */
	public void alignIntake() {
		leftBelt.set(intakeSpeed);
		rightBelt.set(reverseSpeed);
	}
	
	public void toggleFingers() {
		fingerActuator.set(!fingerActuator.get());
	}
	
	public void openClappers() {
		clapperActuator.set(true);
	}
	
	public void closeClappers() {
		clapperActuator.set(false);
	}
	
	
//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
//	the belts, sensors for detecting tote
}
 