package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;

/**
 * @see InvertedEncoder
 * @author Aidan Fay
 */

public class DualEncoder implements PIDSource {
	
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	
	private int directionCorrection = -1;
	
	//right incoder vals are neg, left pos
	
	public DualEncoder (Encoder leftEncoder, Encoder rightEncoder) {
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
	}
	
	@Override
	public double pidGet() {
		return (directionCorrection * leftEncoder.getDistance() - directionCorrection * rightEncoder.getDistance()) / 2;//check if distances are in both positive
	}
	
	public double getRate() {
		return (directionCorrection * leftEncoder.getRate() - directionCorrection * rightEncoder.getRate()) / 2;
	}
	
	public double getAbsoluteRate() {
		return (Math.abs(leftEncoder.getRate()) + Math.abs(rightEncoder.getRate())) / 2;
	}
	
	public void reset() {
		leftEncoder.reset();
		rightEncoder.reset();
	}
	
	public double getDistance() {
		return (directionCorrection * leftEncoder.getDistance() - directionCorrection * rightEncoder.getDistance()) / 2;
	}

}
