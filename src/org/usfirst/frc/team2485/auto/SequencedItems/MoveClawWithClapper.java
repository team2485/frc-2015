package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Claw;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Patrick Wamsley
 */
public class MoveClawWithClapper implements SequencedItem {

	private static final double CHASE_TOLERANCE = 5;
	private static final double kP = 0.0025; 
	private static final double 
			DISTANCE_BETWEEN_CLAW_AND_CLAPPER_ONE_TOTE = 20, // 24.5 - first value, 
			DISTANCE_BETWEEN_CLAW_AND_CLAPPER_STANDARD = 12.25, 
			TOTE_OFFSET								   = 6.5; 
	
	private static Claw claw = Robot.claw;
	
	public static final double UP = .1, DOWN = -.05;  // Direction based off of clapper movement
	private double direction; 
	
	public MoveClawWithClapper(double direction) {
		if (direction == UP || direction == DOWN)
			this.direction = direction; 
		else
			throw new IllegalArgumentException("direction must be up or down"); 
	}
	
	@Override
	public void run() {
		
		int toteCount = Robot.toteCounter.getCount();
		System.out.println("ToteCount is " + toteCount);
		/*
		 * Get actual distance between claw and clapper
		 * Get desired distance between based on tote count
		 * Calculate error (sign matters) (desired - actual)
		 * 
		 */
		if (toteCount == 0 || toteCount == 1 || toteCount == 2) {
			claw.setSetpoint(Claw.ONE_AND_TWO_TOTE_RESTING_POS);
			return;
		}
	
		claw.setManual();
		
		double idealDistance = (Robot.toteCounter.getCount() - 1) * DISTANCE_BETWEEN_CLAW_AND_CLAPPER_STANDARD + TOTE_OFFSET; 
		
		double error = idealDistance - (Robot.claw.getInchHeight() - Robot.clapper.getInchHeight()); 
		
		SmartDashboard.putNumber("claw with clapper error", error);
		
//		if (error <= CHASE_TOLERANCE) 
//			claw.setWinch(0.1); //needs to stay there throughout the sequence. plus, clapper will move again it needs to keep chasing
//		else
		
		if (direction == UP) {
			if (error < 0 || Math.abs(claw.getPotValue() - claw.HIGHEST_POS) < 10) 
				claw.setWinch(0);
			else 
				claw.setWinch(1);
		} else if (direction == DOWN) {
			if (error < 0 && claw.getPotValue() > Claw.ONE_AND_TWO_TOTE_RESTING_POS) 
				claw.setWinch(-1);
			else
				claw.setWinch(0); 
		}
		
	}

	@Override
	public double duration() {
//		System.out.println("isMoving: " + Robot.clapper.isMoving());
		return Robot.clapper.isPIDOnTarget() ? 0 : 1.5;	}

}