package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class RunRollers implements SequencedItem {

	private double speed, timeout; 
	
	public RunRollers(double speed, double timeout) {
		this.speed = speed;
		this.timeout = timeout; 
	}
	
	public RunRollers(double speed) {
		this(speed, .03); 
	}
	
	@Override
	public void run() {
		Robot.rollers.intakeTote(speed);
	}

	@Override
	public double duration() {
		return timeout; 
	}

}
