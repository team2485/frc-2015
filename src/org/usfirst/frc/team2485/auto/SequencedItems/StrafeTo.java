package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

//Need a center encoder to do this. 
public class StrafeTo implements SequencedItem {

	private double inches, yawSetpoint; 
	private boolean finished; 
	private boolean customHeading;
	private double timeout; 
	
	public StrafeTo(double inches, double timeout, double yawSetpoint) {
		this.inches = inches;
		this.timeout = timeout;
		customHeading = true;
		this.yawSetpoint = yawSetpoint;
	}
	
	public StrafeTo(double inches, double timeout) {
		this.inches = inches; 
		finished = false; 
		customHeading = false;
		this.timeout = timeout;
	}
	
	public StrafeTo(double inches) {
		this(inches, 2); 
	}
	
	@Override
	public void run() {
		if(customHeading)
			finished = Robot.drive.strafeToUsingSonicSensor(inches);  
		else
			finished = Robot.drive.strafeToUsingSonicSensor(inches, yawSetpoint);
	}

	@Override
	public double duration() {
		return finished ? 0.03 : timeout; //2 not tested 
	}

}
