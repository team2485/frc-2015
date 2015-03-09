package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class DriveStraight implements SequencedItem {

	private final double distance; //inches
	private boolean finished; 
	
	public DriveStraight(double inches) {
		distance = inches; 
		finished = false; 
	}
	
	@Override
	public void run() {
		finished = Robot.drive.driveTo(distance); 
	}

	@Override
	public double duration() {
		return finished ? 0 : 4; //4 untested
	}

}
