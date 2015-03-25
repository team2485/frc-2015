package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;

/**
 * @author Aidan Fay
 */

public class DualEncoder implements PIDSource {
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	
	private int signFlipHackForPracticeBot = -1;	//TODO: change back to +1 for Valkyrie
	
	//right incoder vals are neg, left pos
	
	public DualEncoder (Encoder leftEncoder, Encoder rightEncoder) {
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
	}
	
	@Override
	public double pidGet() {
		return (signFlipHackForPracticeBot * leftEncoder.getDistance() - signFlipHackForPracticeBot * rightEncoder.getDistance()) / 2;//check if distances are in both positive
	}
	
	public double getRate() {
		return (signFlipHackForPracticeBot * leftEncoder.getRate() - signFlipHackForPracticeBot * rightEncoder.getRate()) / 2;
	}
	
	public double getAbsoluteRate() {
		return (Math.abs(leftEncoder.getRate()) + Math.abs(rightEncoder.getRate())) / 2;
	}
	
	public void reset() {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	public double getDistance() {
		return (signFlipHackForPracticeBot * leftEncoder.getDistance() - signFlipHackForPracticeBot * rightEncoder.getDistance()) / 2;
	}

}
