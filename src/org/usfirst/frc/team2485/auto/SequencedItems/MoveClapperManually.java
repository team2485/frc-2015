package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class MoveClapperManually implements SequencedItem {

	private double speed, time;
	
	public MoveClapperManually(double speed, double time) {
		this.speed = speed;
		this.time = time;
	}
	
	@Override
	public void run() {
		Robot.clapper.liftManually(speed);
	}

	@Override
	public double duration() {
		return time;
	}

}
