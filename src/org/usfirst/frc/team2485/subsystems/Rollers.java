package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;


public class Rollers {
	
	private SpeedController rightWheel, leftWheel;
	
	public Rollers(SpeedController rightWheel, SpeedController leftWheel) {
		this.rightWheel = rightWheel;
		this.leftWheel = leftWheel;
	}
	
	public void intakeTote(double speed) {
		rightWheel.set(-speed);
		leftWheel.set(speed * 1.5);
	}
	
	public void reverseTote(double speed) {
		rightWheel.set(speed);
		leftWheel.set(-speed * 1.5);
	}
	
	public void rotateToteClockwise(double speed) {
		rightWheel.set(-speed);
		leftWheel.set(-speed * 1.5);
	}
	
	public void rotateToteCounterclockwise(double speed) {
		rightWheel.set(speed);
		leftWheel.set(speed * 1.5);
	}
}