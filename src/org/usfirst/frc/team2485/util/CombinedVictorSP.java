package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Patrick Wamsley 
 * @author Ben Clark
 */
public class CombinedVictorSP implements SpeedController {

	private VictorSP[] speedControllerList;
	private double direction = 1;
	
	public CombinedVictorSP(VictorSP... speedControllerList) {
		this.speedControllerList = speedControllerList;
	}
	
	@Override
	public void pidWrite(double output) {	
		set(output); 
	}

	@Override
	public double get() {
		
		double sum = 0;
		
		for(SpeedController s : speedControllerList) 
			sum += s.get(); 
		
		return sum / speedControllerList.length;
	}

	@Override
	public void set(double speed, byte syncGroup) {
		for (SpeedController s : speedControllerList) 
			s.set(speed * direction, syncGroup);
	}

	@Override
	public void set(double speed) {
		for (SpeedController s : speedControllerList) 
			s.set(speed * direction);
	}
	
	public void invertMotorDirection(boolean isInverted) {
		if (isInverted) 
			direction = -1;
		else 
			direction = 1;
	}

	public void disable() {}

}
