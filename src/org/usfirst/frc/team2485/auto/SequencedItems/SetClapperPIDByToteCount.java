package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class SetClapperPIDByToteCount implements SequencedItem {

	private boolean done = false;
	
	@Override
	public void run() {
		Robot.clapper.updateToteCount(Robot.toteCounter.getCount());
		done = true;
	}

	@Override
	public double duration() {
		return done ? 0 : .03;
	}

}
