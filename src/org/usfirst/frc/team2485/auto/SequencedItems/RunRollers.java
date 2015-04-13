package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Ben Clark
 */

public class RunRollers implements SequencedItem {

	private double speed; 
	
	public RunRollers(double speed) {
		this.speed = speed;
	}
	
	@Override
	public void run() {
		Robot.rollers.intakeTote(speed);
	}

	@Override
	public double duration() {
		return .03; 
	}

}
