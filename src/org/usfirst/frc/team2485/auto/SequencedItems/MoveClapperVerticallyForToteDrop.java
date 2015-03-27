package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class MoveClapperVerticallyForToteDrop implements SequencedItem {

	private final double setpoint; 
	private boolean finished = false; 

	/**
	 * @param setpoint. use a public static field from Clapper class
	 * ex: new RaiseClapper(Clapper.TOTE_LEVEL_1); 
	 */
	public MoveClapperVerticallyForToteDrop(double setpoint) {
		this.setpoint = setpoint; 
	}

	@Override
	public void run() {
		Robot.clapper.setSetpoint(setpoint); 
		finished = Robot.clapper.isPIDOnTarget();

	}

	@Override
	public double duration() {
		return finished ? 0.03 : 0.5;
	}

}

