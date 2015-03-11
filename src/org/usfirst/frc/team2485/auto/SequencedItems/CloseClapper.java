package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class CloseClapper implements SequencedItem {

	@Override
	public void run() {
		Robot.clapper.closeClapper();
	}

	@Override
	public double duration() {
		return 0.1;
	}

	
}
