package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSource;

public class ScaledPot implements PIDSource {
	
	private AnalogPotentiometer pot;
	
	
	public ScaledPot(AnalogPotentiometer pot) {
		this.pot = pot;
	}
	
	public double pidGet() {
		return pot.pidGet() * 1000;
	}

}
