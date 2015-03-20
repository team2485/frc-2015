package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class ToteIntake implements SequencedItem {

	private boolean finished; 
	
	public ToteIntake() {
		finished = false; 
	}
	
	@Override
	public void run() {
		Robot.rollers.intakeTote(1);
//		finished = Robot.clapper.toteDetected(); 
	}

	@Override
	public double duration() {
		return finished ? 0 : 2; 
	}

}
