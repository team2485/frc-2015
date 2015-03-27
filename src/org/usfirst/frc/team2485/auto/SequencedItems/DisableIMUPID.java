package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class DisableIMUPID implements SequencedItem {

	@Override
	public void run() {
		Robot.drive.disableIMUPID();

	}

	@Override
	public double duration() {
		// TODO Auto-generated method stub
		return 0.03;
	}

}
