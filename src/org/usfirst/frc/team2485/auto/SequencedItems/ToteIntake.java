package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class ToteIntake implements SequencedItem {

	private boolean finished; 
	
	public ToteIntake(boolean on) {
		finished = false; 
	}
	
	@Override
	public void run() {
		Robot.fingers.dualIntake(1);
	}

	@Override
	public double duration() {
		// TODO Auto-generated method stub
		return 0;
	}

}
