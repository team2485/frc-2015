package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class ExtendRatchet implements SequencedItem {

	@Override
	public void run() {
		Robot.ratchet.extendRatchet(); 
	}

	@Override
	public double duration() {
		return 0.1;
	}

}
