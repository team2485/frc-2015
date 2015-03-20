package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

//we need some sort of sensor to tell when the tote is in/out of the clappper
//until then i dont think we can right this
public class SpitTote implements SequencedItem {

	private boolean finished; 
	
	public SpitTote() {
		finished = false ;
	}
	@Override
	public void run() {
		Robot.rollers.reverseTote(.3 + .04*Robot.toteCounter.getCount()); 
	}

	@Override
	public double duration() {
		return 1;
	}

}
