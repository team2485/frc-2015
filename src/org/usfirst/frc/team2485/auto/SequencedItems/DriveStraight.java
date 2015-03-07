package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class DriveStraight implements SequencedItem {

	private final double distance; //inches
	private boolean finished; 
	
	public DriveStraight(double inches) {
		distance = inches; 
		finished = false; 
	}
	
	public DriveStraight(double feet, double inches) {
		this(feet * 12 + inches); 
	}
	
	@Override
	public void run() {
		finished = Robot.drive.driveTo(distance); 
	}

	@Override
	public double duration() {
		return finished ? 0 : 4; //3 untested
	}

}
