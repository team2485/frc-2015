package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class ReleaseToteStack implements SequencedItem {

	@Override
	public void run() {
		Robot.ratchet.releaseToteStack();
	}

	@Override
	public double duration() {
		return 0;
	}

}
