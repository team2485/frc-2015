package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class SetDriveStraightPID implements SequencedItem {

	private double kP;
	
	public SetDriveStraightPID(double kP) {
		this.kP = kP;
	}
	
	public void run() {
		Robot.drive.setDriveStraightPID(kP, 0, 0);
	}

	@Override
	public double duration() {
		return 0.03;
	}

}
