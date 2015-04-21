package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * 
 * Used to act on multible speed controllers at once, or to treat many speed controllers as one. 
 * 
 * @author Ben Clark
 * @author Patrick Wamsley
 * @author Anoushka Bose
 */
public class CombinedSpeedController implements SpeedController {

	private SpeedController[] speedControllerList;
	private double direction = 1;
	
	public CombinedSpeedController(SpeedController... speedControllerList) {
		this.speedControllerList = speedControllerList;
	}
	
	@Override
	public void pidWrite(double output) {	
		set(output); 
	}

	@Override
	public double get() {
		
		double sum = 0;
		
		for (SpeedController s : speedControllerList) 
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
		direction = isInverted ? -1 : 1; 
	}

	public boolean isMoving() {
		return speedControllerList[0].get() > .05; 
	}
		
	public void disable() {}

}
