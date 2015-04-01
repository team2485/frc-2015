package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicWrapper implements PIDSource {
	private Ultrasonic theHedgehog;
	
	private double lastGoodValue;
	
	public UltrasonicWrapper(Ultrasonic sonic) {
		theHedgehog = sonic;
		lastGoodValue = -1;
	}

	@Override
	public double pidGet() {
		double currValue = theHedgehog.getRangeInches();
		
		if (lastGoodValue == -1 || Math.abs(currValue - lastGoodValue) < 30 ) {
			lastGoodValue = currValue;
			return lastGoodValue;
		}
		else {
//			System.out.println("In UltrasonicWrapper, threw away junk value of: " + currValue);
			return lastGoodValue;
		}
			
		
	}
	
	
}
