package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class SetFingersPos implements SequencedItem {

	private boolean finished; 
	private final int pos; 

	public SetFingersPos(int pos) {
		finished = false; 
		this.pos = pos; 
	}

	@Override
	public void run() {
		if (!finished) {
			Robot.fingers.setFingerPosition(pos);
			finished = true; 
		}
	}

	@Override
	public double duration() {
		return 0.05;
	}
}
