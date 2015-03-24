package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class MoveClapperVertically implements SequencedItem {

	private final double setpoint; 
	private boolean finished = false; 
	
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
		finished = Robot.clapper.isPIDOnTarget();
		System.out.println("From MoveClapperVertically; clapper pid on target = " + finished + " duration " + duration());
	}

	@Override
	public double duration() {
		return finished ? 0.03 : 1.5;
	}

}
