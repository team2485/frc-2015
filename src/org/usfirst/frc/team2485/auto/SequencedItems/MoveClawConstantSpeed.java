package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * Sets the Claw to move at a set speed. Does not time out or stop the claw unless <code>speed = 0</code>. 
 * 
 * @author Patrick Wamsley
 * @see org.usfirst.frc.team2485.auto.SequencedItemsMoveClapperVertically
 */
public class MoveClawConstantSpeed implements SequencedItem {
	
	public MoveClawConstantSpeed(int speed) {
		Robot.claw.liftManually(speed);
	}

	@Override
	public void run() {}

	@Override
	public double duration() {
		return .05; 
	}
	
}
