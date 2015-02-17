package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Claw;

public class MoveClawRelativeToClapper implements SequencedItem {

	private double clawSetpoint;
	
	public MoveClawRelativeToClapper(double clapperSetpoint) {
		double offset = Robot.claw.isOpen() ? Claw.NO_CONTAINER_OFFSET : 0; 
		this.clawSetpoint = Robot.claw.translateClapperSetpoint(clapperSetpoint) + offset;
		
		Robot.claw.setSetpoint(clawSetpoint);
	}
	
	@Override
	public void run() {
		Robot.claw.setSetpoint(clawSetpoint);

	}

	@Override
	public double duration() {
		return Robot.claw.isPidOnTarget() ? 0 : 2; 
	}

}
