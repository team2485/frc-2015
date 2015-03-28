package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Patrick Wamsley
 */

public class MoveClapperVertically implements SequencedItem {

	private final double setpoint; 
	private boolean finished = false; 
	
	private double timeout; 
	
	/**
	 * @param setpoint. use a public static field from Clapper class
	 * ex: new RaiseClapper(Clapper.TOTE_LEVEL_1); 
	 */
	public MoveClapperVertically(double setpoint, double timeout) {
		this.setpoint = setpoint; 
		this.timeout  = timeout; 
	}
	
	public MoveClapperVertically(double setpoint) {
		this(setpoint, 1.5); 
	}

	@Override
	public void run() {
		Robot.clapper.setSetpoint(setpoint); 
		finished = Robot.clapper.isPIDOnTarget();
		
	}

	@Override
	public double duration() {
		return finished ? 0.03 : timeout;
	}

}
