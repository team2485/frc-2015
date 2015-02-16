package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class OpenClaw implements SequencedItem {

	@Override
	public void run() {
		Robot.claw.open(); 
	}

	@Override
	public double duration() {
		return 0.1;
	}

	
}
