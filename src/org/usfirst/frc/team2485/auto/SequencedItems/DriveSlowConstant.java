package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class DriveSlowConstant implements SequencedItem {
	
	@Override
	public void run() {
		Robot.drive.setMotors(0.15, 0.15, 0);
	}

	@Override
	public double duration() {
		return 0.8; // 0.5
	}
	
	

}
