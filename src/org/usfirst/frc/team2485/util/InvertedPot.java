package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSource;

public class InvertedPot implements PIDSource {
	
	private AnalogPotentiometer pot;
	
	
	public InvertedPot(AnalogPotentiometer pot) {
		this.pot = pot;
	}
	
	public double pidGet() {
		return (1-pot.pidGet())*(1000);
	}

}
