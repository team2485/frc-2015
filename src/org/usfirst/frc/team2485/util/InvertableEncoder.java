package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;

/**
 * Used to easily correct encoder direction to prevent rewiring. 
 * 
 * @author Aidan Fay
 */

public class InvertableEncoder implements PIDSource {
	
	private Encoder encoder;
	
	private int signFlip; 
	
	public InvertableEncoder (Encoder encoder, int signFlip) {
		this.encoder = encoder;	
		this.signFlip = signFlip; 
	}
	
	@Override
	public double pidGet() {
		return signFlip * encoder.getDistance();
	}
	
	public double getRate() {
		return signFlip * encoder.getRate();
	}
	
	public double getAbsoluteRate() {
		return Math.abs(encoder.getRate());
	}
	
	public void reset() {
		encoder.reset();
	}
	
	public double getDistance() {
		return signFlip * encoder.getDistance();
	}

}
