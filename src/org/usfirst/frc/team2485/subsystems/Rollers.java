package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;


/**
 * Intake wheels on the end of the clapper. 
 * 
 * @author Ben Clark
 */

public class Rollers {
	
	private SpeedController rightWheel, leftWheel;
	
	public Rollers(SpeedController rightWheel, SpeedController leftWheel) {
		this.rightWheel = rightWheel;
		this.leftWheel = leftWheel;
	}
	
	public void intakeTote(double speed) {
		rightWheel.set(speed);
		leftWheel.set(-speed);
	}
	
	public void reverseTote(double speed) {
		rightWheel.set(-speed);
		leftWheel.set(speed);
	}
	
	public void rotateToteClockwise(double speed) {
		rightWheel.set(speed);
		leftWheel.set(speed);
	}
	
	public void rotateToteCounterclockwise(double speed) {
		rightWheel.set(-speed);
		leftWheel.set(-speed);
	}
}