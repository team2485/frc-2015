package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * Runs the collers at the set speed. <p>
 * 
 * WARNING: Makes no attempt to stop the rollers after a certain amount of time. 
 * 			To stop the rollers, contruct a <code> RunRollers </code> instance with a speed of 0. 
 * 			<p> <code> new RunRollers(0) //stops rollers
 * 
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
