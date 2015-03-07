package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class TiltStrongback implements SequencedItem {

	private double setpoint;
	
	public TiltStrongback(double setpoint) {
		this.setpoint = setpoint;
		Robot.strongback.setSetpoint(setpoint);
		Robot.strongback.enablePid();
	}
	
	@Override
	public void run() {
		Robot.strongback.enablePid();
		Robot.strongback.setSetpoint(setpoint);
	}

	@Override
	public double duration() {
		return .05;
	}

}
