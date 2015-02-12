package org.usfirst.frc.team2485.util;

public class ThresholdHandler { 
	
	
	
	/**
	 * Non lossy thresholding.
	 * If the threshold is 0.1 than and val is 0.1 than it will return 0
	 * @param val
	 * @param threshold
	 * @return
	 */
	public static double handleThreshold(double val, double threshold) {
		double returnValue = (Math.abs(val) > Math.abs(threshold)) ? (val/Math.abs(val)*(Math.abs(val)-threshold)/(1-threshold)) : 0.0;
		return returnValue;
	}
}
