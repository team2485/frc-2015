package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Claw;

public class MoveClawWithToteIntake implements SequencedItem {
	
	private boolean finished = false; 
	private final double setpoint; 
	
	public MoveClawWithToteIntake() {
		switch (Robot.toteCounter.getCount()) {
			case 1:
				setpoint = Claw.ONE_TOTE_LOADING;
				break;
			case 2:
				setpoint = Claw.TWO_TOTE_LOADING;
				break;
			case 3: 
				setpoint = Claw.THREE_TOTE_LOADING;
				break;
			case 4: 
				setpoint = Claw.FOUR_TOTE_LOADING;
				break;
			case 5: 
				setpoint = Claw.HIGHEST_POS;
				break;
			case 6:
				setpoint = Claw.HIGHEST_POS;
				break;
			default: 
				throw new IllegalStateException("We must have 1 - 6 totes.");
		}
	}

	@Override
	public void run() {
		
		Claw claw = Robot.claw; 
		
		claw.setSetpoint(setpoint);
		claw.updateWinchPeriodic();
//		finished = claw.isPidOnTarget(); // TODO: fix duration and finishing condition 
	}

	@Override
	public double duration() {
		return finished ? 0 : 2; //2 untested 
	}
	
}
