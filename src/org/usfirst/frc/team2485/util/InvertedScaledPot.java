package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * 
 * Used to invert the direction of a pot, as well as scale the units. 
 * 
 * @see ScaledPot
 * @author Anoushka Bose
 */

public class InvertedScaledPot implements PIDSource {
	
	private AnalogPotentiometer pot;
	
	private static final double SCALAR = 1000; 
	
	public InvertedScaledPot(AnalogPotentiometer pot) {
		this.pot = pot;
	}
	
	@Override
	public double pidGet() {
		return (1 - pot.pidGet()) * (SCALAR);
	}

}
