package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Aidan Fay
 */

public class SetClapperPID implements SequencedItem {
	
	private double kP;
	private double kI;
	private double kD;

	public SetClapperPID (double kP, double kI, double kD){
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	@Override
	public void run() {
		Robot.clapper.setPID(kP, kI, kD);
	}

	@Override
	public double duration() {
		return 0.05;
	}

}
