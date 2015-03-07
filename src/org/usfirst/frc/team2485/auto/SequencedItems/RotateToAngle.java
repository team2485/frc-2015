package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class RotateToAngle implements SequencedItem {
	
	private final double angle; 
	private boolean finished; 
	
	public RotateToAngle(double angle) {
		this.angle = angle; 
		finished = false; 
	}

	@Override
	public void run() {
		finished = Robot.drive.rotateTo(angle); 
	}

	@Override
	public double duration() {
		return finished ? 0 : 3; 
	}
	
	
}
