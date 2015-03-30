package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class DriveBackAndDropTotesForAuto implements SequencedItem {

	
	private boolean finished = false;
	private double speed, error, timeout; 
	
	public DriveBackAndDropTotesForAuto(double maxSpeed, double timeout) {
		this.speed = maxSpeed;
		this.timeout = timeout;
		this.error = 150; 
	}
	
	public DriveBackAndDropTotesForAuto(double speed) {
		this(speed, 1); 
	}
	
	@Override
	public void run() {
		
		error = Math.abs(Robot.drive.getDistanceFromEncoders() - 150); 
		
		if (error > 20)
			Robot.drive.setLeftRight(speed, speed);
		else if (error > 5) 
			Robot.drive.setLeftRight(-error * .04, -error * .04);
		else {
			Robot.clapper.openClapper(); 
			Robot.rollers.reverseTote(.3); 
			finished = true; 
		}
	}

	@Override
	public double duration() {
		return finished ? .1 : timeout;
	}

	
}
