package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.VictorSP;


public class Rollers {
	
	private VictorSP rightWheel, leftWheel;
	
	public Rollers(VictorSP rightWheel, VictorSP leftWheel) {
		this.rightWheel = rightWheel;
		this.leftWheel = leftWheel;
	}
	
	public Rollers(int rightWheelChannel, int leftWheelChannel) {
		this(new VictorSP(rightWheelChannel), new VictorSP(leftWheelChannel));
	}
	
	//TODO: figure out if the motors need to be inverted
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
