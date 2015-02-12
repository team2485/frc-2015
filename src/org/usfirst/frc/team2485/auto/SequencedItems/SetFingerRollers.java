package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class SetFingerRollers implements SequencedItem {

	public static final int INTAKE = 0, REVERSE = 1; 
	private int type; 
	
	public SetFingerRollers(int type) {
		if (type == INTAKE || type == REVERSE)
			this.type = type;
		else
			throw new IllegalArgumentException("Must send rollers intake or reverse"); 
	}
	@Override
	public void run() {
		if (type == INTAKE)
			Robot.fingers.dualIntake(1);
		else if (type == REVERSE) 
			Robot.fingers.dualReverse(1);
		else
			throw new IllegalStateException("Finger rollers can only go intake or reverse");
	}

	@Override
	public double duration() {
		return 1; //TODO: test timing, knowing what we are doing  
	}

	
}
