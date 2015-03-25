package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class ZeroEncoders implements SequencedItem {

	@Override
	public void run() {
		Robot.drive.resetEncoders();
	}

	@Override
	public double duration() {
		// TODO Auto-generated method stub
		return 0.05;
	}

}
