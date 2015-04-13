package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * 
 * Used to Scale Pot values into more useable units. 
 * 
 * @author Anoushka Bose
 */

public class ScaledPot implements PIDSource {
	
	private AnalogPotentiometer pot;
	
	private static final double SCALER = 1000; 
	
	public ScaledPot(AnalogPotentiometer pot) {
		this.pot = pot;
	}
	
	@Override
	public double pidGet() {
		return pot.pidGet() * SCALER;
	}

}
