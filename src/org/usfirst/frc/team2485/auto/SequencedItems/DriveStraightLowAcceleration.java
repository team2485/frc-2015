package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class DriveStraightLowAcceleration implements SequencedItem {
	
	private boolean finished;
	private double distance; 
	
	public DriveStraightLowAcceleration(double inches) {
		
		finished = false;
		distance = inches; 
		
		Robot.drive.driveStraightPID.setOutputRange(-.5, .5); 
	}
	
	public DriveStraightLowAcceleration(double feet, double inches) {
		this(12 * feet + inches); 
	}

	@Override
	public void run() {
		if (Robot.drive.driveTo(distance)) {
			finished = true; 
			Robot.drive.driveStraightPID.setOutputRange(-1, 1); 
		}
	}

	@Override
	public double duration() {
		return finished ? 0 : 2; 
	} 
	
	

}
