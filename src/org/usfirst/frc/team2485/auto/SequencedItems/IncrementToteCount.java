package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Ben Clark
 */

public class IncrementToteCount implements SequencedItem {

	private int amount; 
	private boolean finished; 
	
	public IncrementToteCount(int amount) {
		this.amount = amount; 
		finished = false; 
	}
	
	public IncrementToteCount() {
		this(1); 
	}
	
	@Override
	public void run() {
		if (!finished)
			Robot.toteCounter.addTote(amount);
		finished = true;
	}

	@Override
	public double duration() {
		return .03;
	}

}
