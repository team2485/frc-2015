package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class DriveAtSetSpeed implements SequencedItem {
	
	private double power, time; 
	
	public DriveAtSetSpeed(double power, double time) {
		this.power = power; 
		this.time  = time; 
	}
	
	@Override
	public void run() {
		Robot.drive.setMotors(power, power , 0);
	}

	@Override
	public double duration() {
		return time; 
	}
	
	

}
