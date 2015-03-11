package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Aidan Fay
 */
public class ResetCommandeerer implements SequencedItem{

	@Override
	public void run() {
		Robot.containerCommandeerer.resetSol();
	}

	@Override
	public double duration() {
		return 0.1;
	}

}
