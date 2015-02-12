package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Claw;

public class MoveClawVertically implements SequencedItem {
	
	private boolean finished; 
	private final int setpoint; 
	
	public MoveClawVertically(int setpoint) {
		this.setpoint = setpoint; 
		finished = false; 
	}

	@Override
	public void run() {
		
		Claw claw = Robot.claw; 
		
		claw.setSetpoint(setpoint);
		finished = claw.isPidOnTarget(); 
	}

	@Override
	public double duration() {
		return finished ? 0 : 2; //2 untested 
	}
	
}
