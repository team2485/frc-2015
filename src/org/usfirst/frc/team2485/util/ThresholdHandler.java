package org.usfirst.frc.team2485.util;

/**
 * @author Patrick Wamsley
 * @author Anoushka Bose
 * @author Aidan Fay
 */
public class ThresholdHandler { 
	
	/**
	 * Non lossy thresholding.
	 * If the threshold is 0.1 than and val is 0.1 than it will return 0
	 * @param val
	 * @param threshold
	 * @return
	 */
	
	public static final double STANDARD_THRESHOLD = 0.1; 
	@Deprecated
	public static double handleThreshold(double val, double threshold) {
		return (Math.abs(val) > Math.abs(threshold)) ? (val / Math.abs(val) * (Math.abs(val) - threshold) / (1 - threshold)) : 0.0;
	}
	@Deprecated
	public static double handleThresholdNonLinear(double val, double threshold) {
		double returnValue = (Math.abs(val) > Math.abs(threshold)) ?
				(val / Math.abs(val) * (Math.abs(val) - threshold) / (1-threshold)) : 0.0;
		returnValue *= Math.abs(returnValue);
		return returnValue;
	}
	
	/**
	 * Thresholds and scales values linearly: <code>(max - min) / (1 - t) * v + min;</code> where <code>(max - min) / (1 - t)</cdoe> is the slope.
	 * Derived from fact that an input value of the threshold should return min : ( <code>f(threashold) = min</code> )
	 * and an input value of 1 should return max : ( <code> f(1) = max </code> ) 
	 *  
	 * @param val = value from input
	 * @param threshold = deadband threashold 
	 * @param absoluteMin = absolute value of min range
	 * @param absoluteMax = absolute value of max range 
	 * 
	 * 
	 * @return Corrected input from joystick. If input is below threashold, 0 is returned. 
	 * 		   If not, input is scaled between [min, max]. 
	 * 
	 */
	public static double deadbandAndScale(double val, double threshold, double absoluteMin, double absoluteMax) {
		
		if (Math.abs(val) <= threshold)
			return 0; 
		
		double returnVal = ((absoluteMax - absoluteMin) / (1 - threshold) * Math.abs(val)) + absoluteMin; 
		
		return val > 0 ? returnVal : -returnVal; 
	}
	
}
