package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;

public class DualEncoder implements PIDSource {
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	
	//right incoder vals are neg, left pos
	
	public DualEncoder (Encoder leftEncoder, Encoder rightEncoder) {
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
	}
	@Override
	public double pidGet() {
		return (leftEncoder.getDistance() - rightEncoder.getDistance())/2;//check if distances are in both positive
	}
	
	public double getRate(){
		return (leftEncoder.getRate() - rightEncoder.getRate())/2;
	}
	
	public void reset(){
		leftEncoder.reset();
		rightEncoder.reset();
	}
	public double getDistance() {
		return (leftEncoder.getDistance() - rightEncoder.getDistance())/2;
	}

}
