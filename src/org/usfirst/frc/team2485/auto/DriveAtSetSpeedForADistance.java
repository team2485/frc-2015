package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.robot.Robot;

public class DriveAtSetSpeedForADistance implements SequencedItem {

	private double power, distance;
	private boolean finished;
	
	public DriveAtSetSpeedForADistance(double power, double distance) {
		this.power = power;
		this.distance = distance;
		this.finished = false;
	}
	
	@Override
	public void run() {
		Robot.drive.setLeftRight(power, power);
		
		double distanceTraveled = Robot.drive.getDistanceFromEncoders();
		if(Math.abs(distanceTraveled) >= Math.abs(distance))
			finished = true;
	}

	@Override
	public double duration() {
		// TODO Auto-generated method stub
		return finished ? 0.03 : 4;
	}

}
