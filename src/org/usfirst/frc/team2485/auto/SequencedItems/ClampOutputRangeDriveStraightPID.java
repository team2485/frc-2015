package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class ClampOutputRangeDriveStraightPID implements SequencedItem {

	private double min, max; 
	
	public ClampOutputRangeDriveStraightPID(double min, double max) {
		this.min = min; 
		this.max = max; 
	}
	@Override
	public void run() {
		Robot.drive.setOutputRange(min, max); 
	}

	@Override
	public double duration() {
		return 0.05;
	}

	
}
