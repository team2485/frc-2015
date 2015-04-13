package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Claw;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Patrick Wamsley
 */
public class MoveClawWithClapper implements SequencedItem {

	private static final double slopeForOutputRamp = 0.6; 
	private static final double 
			DISTANCE_BETWEEN_CLAW_AND_CLAPPER_STANDARD = 12.25, 
			TOTE_OFFSET								   = 6.5; 
	
	private static Claw claw = Robot.claw;
	
	public static final int UP = 1, DOWN = -1;  // Direction based off of clapper movement
	private int direction; 
	
	private boolean finished;
	
	public MoveClawWithClapper(int direction) {
		if (direction == UP || direction == DOWN)
			this.direction = direction; 
		else
			throw new IllegalArgumentException("direction must be up or down"); 
		
		finished = false;
	}
	
	@Override
	public void run() {
		
		int toteCount = Robot.toteCounter.getCount();

		finished = Robot.clapper.isPIDOnTarget();
		
		if (toteCount == 0 || toteCount == 1 || toteCount == 2) { 
			claw.setSetpoint(Claw.ONE_AND_TWO_TOTE_RESTING_POS);
			return;
		}
		
		System.out.println("From MoveClawWithClapper; clapper pid on target = " + finished + " duration " + duration());
		
//		if (toteCount >3) {
//			claw.setSetpoint(Claw.HIGHEST_POS - 5);
//			return; //DELETE THIS ONCE THE LOGIC GETS FIXED SO THAT INTAKING THE 5TH TOTE WORKS
//		}
	
		if (finished) {
			//claw.setSetpoint(Claw.magicSetpointPositionofawesome * toteCountMaybe; 
			System.out.println("finished in MoveClawWithClapper");
		} else {
			claw.setManual();
			
			/*
			 * Get actual distance between claw and clapper
			 * Get desired distance between based on tote count
			 * Calculate error (sign matters) (desired - actual)
			 * 
			 */
			
			double tempDistBuffer = 4;
			
			double idealDistance = tempDistBuffer + (Robot.toteCounter.getCount() - 1) * DISTANCE_BETWEEN_CLAW_AND_CLAPPER_STANDARD + TOTE_OFFSET; 
			
			double actualDistance = Robot.claw.getInchHeight() - Robot.clapper.getInchHeight(); 
			
			double error = idealDistance - actualDistance; 
//			
//			SmartDashboard.putNumber("Ideal distance: ", idealDistance);
//			SmartDashboard.putNumber("actual distance ", actualDistance);
//			
			double topWinchSpeed = .55;
			
			double safetyTolerance = .5; 
			
			if (direction == UP) {
				if (error < 0 || Math.abs(claw.getPotValue() - Claw.HIGHEST_POS) < 
						Claw.POTS_PER_INCH * safetyTolerance) {
					double winchOutput = topWinchSpeed + slopeForOutputRamp * error; 
					if (winchOutput < 0 ||  Math.abs(claw.getPotValue() - Claw.HIGHEST_POS) <
							Claw.POTS_PER_INCH * safetyTolerance)
						winchOutput = 0; 
					claw.setWinch(winchOutput); 
				}
				else 
					claw.setWinch(topWinchSpeed);
				
			} else if (direction == DOWN) {
				if (error < 0 && claw.getPotValue() > Claw.MANUAL_SAFETY_ABOVE_RACHET_POS) 
					claw.setWinch(-topWinchSpeed);
				else {
					double winchOutput = -topWinchSpeed + slopeForOutputRamp * error; 
					if (winchOutput > 0)
						winchOutput = 0; 
					claw.setWinch(winchOutput);
				}
			}
		} 
	}

	@Override
	public double duration() {
//		System.out.println("isMoving: " + Robot.clapper.isMoving());
		return finished ? 0.03 : 1.5;	
		}

}