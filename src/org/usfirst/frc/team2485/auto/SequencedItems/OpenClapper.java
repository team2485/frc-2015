package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class OpenClapper implements SequencedItem {

	@Override
	public void run() {
		Robot.clapper.openClapper();
	}

	@Override
	public double duration() {
		return .1; 
	}
	
}
