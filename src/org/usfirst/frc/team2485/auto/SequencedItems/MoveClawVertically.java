package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Claw;

/**
 * @author Patrick Wamsley
 */

public class MoveClawVertically implements SequencedItem {
	
	private boolean finished = false; 
	private final double setpoint; 
	
	public MoveClawVertically(double setpoint) {
		this.setpoint = setpoint;
	}

	@Override
	public void run() {
		
		Claw claw = Robot.claw; 
		
		claw.setSetpoint(setpoint);
		claw.updateWinchPeriodic();
		finished = claw.isPidOnTarget(); 
	}

	@Override
	public double duration() {
		return finished ? 0 : 1.5; 
	}
	
}