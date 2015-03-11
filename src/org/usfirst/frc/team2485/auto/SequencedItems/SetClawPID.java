package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Aidan Fay
 */

public class SetClawPID implements SequencedItem {
	
	private double kP;
	private double kI;
	private double kD;

	public SetClawPID (double kP, double kI, double kD){
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		Robot.claw.setPID(kP, kI, kD);
	}

	@Override
	public void run() {
		Robot.claw.setPID(kP, kI, kD);
	}

	@Override
	public double duration() {
		return 0.05;
	}

}
