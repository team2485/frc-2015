package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;

public class StrafeTo implements SequencedItem {

	private final double distance; 
	private boolean finished; 
	
	public StrafeTo(double inches) {
		distance = inches; 
		finished = false; 
		
	}
	@Override
	public void run() {
//		finished = Robot.drive.strafeTo(distance);  
	}

	@Override
	public double duration() {
		return finished ? 0 : 4; //4 not tested 
	}

}
