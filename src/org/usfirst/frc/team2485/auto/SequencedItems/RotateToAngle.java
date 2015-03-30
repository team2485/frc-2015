package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class RotateToAngle implements SequencedItem {
	
	private final double angle; 
	private boolean finished;
	private final double timeout; 
	
	public RotateToAngle(double angle, double timeout) {
		this.angle = angle; 
		finished = false;
		this.timeout = timeout;
	}
	
	public RotateToAngle(double angle) {
		 this(angle, 2);
	}

	@Override
	public void run() {
		finished = Robot.drive.rotateTo(angle); 
	}

	@Override
	public double duration() {
		return finished ? 0 : timeout; 
	}
	
	
}
