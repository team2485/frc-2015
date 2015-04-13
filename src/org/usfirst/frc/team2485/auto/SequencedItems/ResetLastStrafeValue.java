package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Anoushka Bose 
 */
public class ResetLastStrafeValue implements SequencedItem {

	@Override
	public void run() {
		Robot.drive.resetLastStrafeValue();
	}

	@Override
	public double duration() {
		return 0.03;
	}

}
