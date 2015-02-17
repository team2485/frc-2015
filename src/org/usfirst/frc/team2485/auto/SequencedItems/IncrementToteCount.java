package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class IncrementToteCount implements SequencedItem {

	private boolean done = false;
	
	public IncrementToteCount() {
		Robot.toteCounter.addTote();
	}
	
	@Override
	public void run() {
		done = true;
	}

	@Override
	public double duration() {
		// TODO Auto-generated method stub
		return done ? 0 : .05;
	}

}
