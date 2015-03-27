package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class DisableDriveStraightPID implements SequencedItem {

	@Override
	public void run() {
		Robot.drive.disableDriveStraightPID();
//		Robot.drive.disableIMUPID();
	}
	
	@Override
	public double duration() {
		return 0.05; 
	}
}
