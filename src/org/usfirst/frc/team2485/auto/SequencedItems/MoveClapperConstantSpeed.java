package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * Moves the clappers manually at a constant speed. Does not time out or stop the clappers unless <code> speed = 0 </code>.
 * 
 * @author pwamsley
 * @see org.usfirst.frc.team2485.auto.SequencedItems.MoveClawConstantSpeed 
 */

public class MoveClapperConstantSpeed implements SequencedItem {
	
	private double speed, timeout;

	public MoveClapperConstantSpeed(double speed) {
		this(speed, .03);
		}
	
	public MoveClapperConstantSpeed(double speed, double timeout) {
		this.speed = speed;
		this.timeout = timeout;
	}
	
	@Override
	public void run() {
		Robot.clapper.liftManually(speed);
	}

	@Override
	public double duration() {
		return timeout; 
	}

}
