package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Ben Clark
 */

public class IncrementToteCount implements SequencedItem {

	private boolean done = false;
	private int amount; 
	
	public IncrementToteCount(int amount) {
		this.amount = amount; 
	}
	
	public IncrementToteCount() {
		Robot.toteCounter.addTote(amount);
	}
	
	@Override
	public void run() {
		done = true;
	}

	@Override
	public double duration() {
		return done ? 0 : .05;
	}

}
