package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class MoveClawConstantSpeed implements SequencedItem {
	
	private int time, speed; 
	private boolean killed; 
	
	public MoveClawConstantSpeed(int time, int speed) {
		this.time = time; 
		this.speed = speed; 
		
		killed = false; 
	}

	@Override
	public void run() {
		Robot.claw.liftManually(speed);
	}

	@Override
	public double duration() {
		return killed ? 0 : time;
	}
	
	public void kill() {
		killed = true; 
	}

	
}
