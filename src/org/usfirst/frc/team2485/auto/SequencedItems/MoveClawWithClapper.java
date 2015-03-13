package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Claw;

/**
 * @author Patrick Wamsley
 */
public class MoveClawWithClapper implements SequencedItem {

	private static final double CHASE_TOLERANCE = 1;
	private static final double kP = 0.12; 
	private static final double 
			DISTANCE_BETWEEN_CLAW_AND_CLAPPER_ONE_TOTE = 20, 
			DISTANCE_BETWEEN_CLAW_AND_CLAPPER_STANDARD = 12; 
	
	private static Claw claw = Robot.claw;
	
	public static final double UP = .1, DOWN = -.05;  
	private double direction; 
	

	
	public MoveClawWithClapper(double direction) {
		if (direction == UP || direction == DOWN)
			this.direction = direction; 
		else
			throw new IllegalArgumentException("direction must be up or down"); 
	}
	
	@Override
	public void run() {
		
		double idealDistance = Robot.toteCounter.getCount() == 1 ? DISTANCE_BETWEEN_CLAW_AND_CLAPPER_ONE_TOTE :
			DISTANCE_BETWEEN_CLAW_AND_CLAPPER_STANDARD; 
		
		double error = idealDistance - (Robot.clapper.getInchHeight() - claw.getInchHeight()); 
		
//		if (error <= CHASE_TOLERANCE) 
//			claw.setWinch(0.1); //needs to stay there throwout the sequence. plus, clapper will move again it needs to keep chasing
//		else
		
		claw.setWinch(direction + (error * kP));
	}

	@Override
	public double duration() {
		return !(Robot.clapper.isMoving()) ? 0 : 1; //untested 
	}

}
