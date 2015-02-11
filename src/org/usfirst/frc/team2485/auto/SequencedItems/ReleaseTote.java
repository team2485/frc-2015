package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

//we beed some sort of sensor to tell when the tote is in/out of the clappper
//until then i dont think we can right this
public class ReleaseTote implements SequencedItem {

	private boolean finished; 
	
	public ReleaseTote() {
		finished = false ;
	}
	@Override
	public void run() {
		Robot.fingers.dualReverse(1); 
	}

	@Override
	public double duration() {
		// TODO Auto-generated method stub
		return 0;
	}

}
