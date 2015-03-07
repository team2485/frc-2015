package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class MoveClapperVertically implements SequencedItem {

	private final double setpoint; 
	
	/**
	 * @param setpoint. use a public static field from Clapper class
	 * ex: new RaiseClapper(Clapper.TOTE_LEVEL_1); 
	 */
	public MoveClapperVertically(double setpoint) {
		this.setpoint = setpoint; 
	}

	@Override
	public void run() {
		Robot.clapper.setSetpoint(setpoint); 
//		System.out.println("here in Move Clapper, PID finished is " + Robot.clapper.isPIDOnTarget() +
//				" error: " + Robot.clapper.getError());
//		System.out.println("actual clapper output is " + Robot.clapper.getMotorOutput());
	}

	@Override
	public double duration() {
		return Robot.clapper.isPIDOnTarget() ? 0 : 1.0;
	}
	
	
}
