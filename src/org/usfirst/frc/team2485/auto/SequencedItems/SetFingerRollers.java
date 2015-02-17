package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class SetFingerRollers implements SequencedItem {

	public static final int INTAKE = 0, REVERSE = 1, OFF = 2; 
	private int type; 
	private double timing, speed;
	private boolean done;
	
//	private static int numTotes = 0; 
	
	public SetFingerRollers(int type, double timing, double speed) {
		if (type == INTAKE || type == REVERSE || type == OFF)
			this.type = type;
		else
			throw new IllegalArgumentException("Must send rollers intake or reverse or off"); 
		this.speed = speed;
		this.timing = timing; 
		
//		numTotes++; 
	}
	@Override
	public void run() {
		
		done = Robot.clapper.toteDetected();
		
		if (type == INTAKE)
			Robot.fingers.dualIntake(speed); 
		else if (type == REVERSE) 
			Robot.fingers.dualReverse(speed);
		else if (type == OFF)
			Robot.fingers.dualIntake(0); 
		else
			throw new IllegalStateException("Finger rollers can only go intake, reverse, or off");
	}

	@Override
	public double duration() {
		return done ? 0 : timing;
	}

	
}
