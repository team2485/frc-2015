package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class OpenClapper implements SequencedItem {
	
	private boolean finished; 
	
	public OpenClapper() {
		finished = false; 
	}

	@Override
	public void run() {
		Robot.clapper.openClapper();
		finished = Robot.clapper.isOpen(); 
	}

	@Override
	public double duration() {
		return finished ? 0 : 1; //1 untested
	}
	
}
