package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class DriveStraight implements SequencedItem {

	private double inches, yawSetpoint; 
	private boolean finished; 
	private boolean customHeading;
	
	private double timeout; 
	 
	public DriveStraight(double inches, double timeout, double yawSetpoint) {
		this.inches = inches;
		this.timeout = timeout;
		this.yawSetpoint = yawSetpoint;
		customHeading = true;
	}
	
	public DriveStraight(double inches, double timeout) {
		this.inches = inches; 
		finished = false; 
		customHeading = false;
		this.timeout = timeout; 
	}
	
	public DriveStraight(double inches) {
		this(inches, 4); 
	}
	
	@Override
	public void run() {
		if(customHeading)
			finished = Robot.drive.driveTo(inches, yawSetpoint); 
		else
			finished = Robot.drive.driveTo(inches);
	}

	@Override
	public double duration() {
		return finished ? 0 : timeout; 
	}

}
