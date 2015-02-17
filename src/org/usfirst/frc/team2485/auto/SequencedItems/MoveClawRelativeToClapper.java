package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class MoveClawRelativeToClapper implements SequencedItem {

	private double clapperSetpoint;
	
	public MoveClawRelativeToClapper(double clapperSetpoint) {
		this.clapperSetpoint = clapperSetpoint;
		Robot.claw.setSetpoint(Robot.claw.translateClapperSetpoint(clapperSetpoint));
	}
	
	@Override
	public void run() {
		Robot.claw.setSetpoint(Robot.claw.translateClapperSetpoint(clapperSetpoint));

	}

	@Override
	public double duration() {
		return Robot.claw.isPidOnTarget() ? 0 : 2; 
	}

}
