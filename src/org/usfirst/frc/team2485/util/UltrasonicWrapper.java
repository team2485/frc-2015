package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 * 
 * Prevents bad data jumps from Ultrasonic sensor. <p>
 * 
 * WARNING: If sonic's initial data is bad, this prevents ever getting good data. 
 * 
 * @author Patrick Wamsley
 */

public class UltrasonicWrapper implements PIDSource {
	
	private Ultrasonic sonic; //aka the hedgehog
	
	private static final double MAX_SENSOR_VALUE_CHANGE = 40; 
	
	private double lastGoodValue;
	
	public UltrasonicWrapper(Ultrasonic sonic) {
		this.sonic = sonic;
		lastGoodValue = -1;
	}

	@Override
	public double pidGet() {
		
		double currValue = sonic.getRangeInches();
		
		if (lastGoodValue == -1 || Math.abs(currValue - lastGoodValue) < MAX_SENSOR_VALUE_CHANGE) {
			lastGoodValue = currValue;
			return lastGoodValue;
		} else {
//			System.out.println("In UltrasonicWrapper, threw away junk value of: " + currValue);
			return lastGoodValue;
		}		
	}
}
