package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Clapper;

public class DriveBackAndDropTotesForAuto implements SequencedItem {

	
	private boolean finished = false;
	private double speed, error, timeout; 
	
	public DriveBackAndDropTotesForAuto(double maxSpeed, double timeout) {
		this.speed = maxSpeed;
		this.timeout = timeout;
		this.error = 50; //testing the drop seq; this needs to be about 150
	}
	
	public DriveBackAndDropTotesForAuto(double speed) {
		this(speed, 1); 
	}
	
	@Override
	public void run() {
		
		error = Math.abs(Robot.drive.getDistanceFromEncoders() + 140); 
		
		System.out.println("error in DriveBackAndDropTotesForAuto = " + error);
		
		if (error > 70) {
			Robot.drive.setLeftRight(speed, speed);
			Robot.clapper.setSetpoint(Clapper.ABOVE_RATCHET_SETPOINT + 20);
			Robot.ratchet.retractRatchet();
		}
		
		else if (error > 55) {
			Robot.drive.setLeftRight(speed, speed);
			Robot.clapper.setSetpoint(Clapper.LOADING_SETPOINT);
		}
		else if (error > 20) {
			Robot.drive.setLeftRight(-.5, -.5);
			Robot.rollers.reverseTote(.5); 
		}
		else if (error > 0) {
			Robot.drive.setLeftRight(-.3, -.3);
			Robot.clapper.openClapper(); 
			finished = true; 
		}
		else 
			Robot.drive.setLeftRight(0, 0); //happiness
	}

	@Override
	public double duration() {
		return finished ? .1 : timeout;
	}

	
}
