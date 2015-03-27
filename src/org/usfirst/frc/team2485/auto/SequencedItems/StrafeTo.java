package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

//Need a center encoder to do this. 
public class StrafeTo implements SequencedItem {

	private final double distance; 
	private boolean finished; 
	
	private double timeout; 
	
	public StrafeTo(double inches, double timeout) {
		distance = inches; 
		finished = false; 
		
		this.timeout = timeout;
	}
	
	public StrafeTo(double inches) {
		this(inches, 2); 
	}
	
	@Override
	public void run() {
		finished = Robot.drive.strafeTo(distance);  
	}

	@Override
	public double duration() {
		return finished ? 0 : timeout; //2 not tested 
	}

}
