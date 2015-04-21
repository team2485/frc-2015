package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class SetRollers implements SequencedItem {

	public static final int INTAKE = 0, REVERSE = 1, OFF = 2, LEFT = 3, RIGHT = 4; 
	private int type; 
	private double speed;
	
	public SetRollers(int type, double speed) {
		if (type == INTAKE || type == REVERSE || type == OFF || type == LEFT || type == RIGHT)
			this.type = type;
		else
			throw new IllegalArgumentException("Must send rollers intake or reverse or off"); 
		
		this.speed = speed;

	}
	@Override
	public void run() {
		
		if (type == INTAKE)
			Robot.rollers.intakeTote(speed); 
		else if (type == REVERSE) 
			Robot.rollers.reverseTote(speed);
		else if (type == OFF)
			Robot.rollers.intakeTote(0); 
		else if (type == LEFT)
			Robot.rollers.rotateToteClockwise(speed);
		else if (type == RIGHT)
			Robot.rollers.rotateToteCounterclockwise(speed);
		else
			throw new IllegalStateException("Finger rollers can only go intake, reverse, or off");
	}

	@Override
	public double duration() {
		return .05;
	}

}
