package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class DriveStraight implements SequencedItem {

	private final double distance; //inches
	private boolean finished; 
	
	private double timeout; 
	
	public DriveStraight(double inches, double timeout) {
		distance = inches; 
		finished = false; 
		
		this.timeout = timeout; 
	}
	
	public DriveStraight(double inches) {
		this(inches, 4); 
	}
	
	@Override
	public void run() {
		finished = Robot.drive.driveTo(distance); 
	}

	@Override
	public double duration() {
		return finished ? 0 : timeout; 
	}

}
