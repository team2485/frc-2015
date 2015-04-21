package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class DropCenterWheel implements SequencedItem {

	private boolean down;
	
	public DropCenterWheel(boolean down) {
		this.down = down;
			
	}
	@Override
	public void run() {
		Robot.drive.dropCenterWheel(down);
	}

	@Override
	public double duration() {
		return 0.1;
	}

}
