package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;

/**
 * @author Aidan Fay
 */

public class InvertableEncoder implements PIDSource {
	
	private Encoder encoder;
	
	
	private int signFlipHackForPracticeBot = -1;	//TODO: check changing this back to +1 for Valkyrie
	
	//right incoder vals are neg, left pos
	
	public InvertableEncoder (Encoder encoder) {
		this.encoder = encoder;		
	}
	
	@Override
	public double pidGet() {
		return signFlipHackForPracticeBot * encoder.getDistance();
	}
	
	public double getRate() {
		return signFlipHackForPracticeBot * encoder.getRate();
	}
	
	public double getAbsoluteRate() {
		return Math.abs(encoder.getRate());
	}
	
	public void reset() {
		encoder.reset();
	}
	
	public double getDistance() {
		return signFlipHackForPracticeBot * encoder.getDistance();
	}

}
