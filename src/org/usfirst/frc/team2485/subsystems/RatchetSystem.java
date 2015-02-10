package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * 
 * @author Ben Clark
 */
public class RatchetSystem {

	private Solenoid ratchetActuator;
	
	public RatchetSystem(Solenoid ratchetActuator) {
		this.ratchetActuator = ratchetActuator;
	}
	
	public RatchetSystem(int ratchetActuatorPort) {
		this(new Solenoid(ratchetActuatorPort));
	}
	
	public void releaseToteStack() {
		ratchetActuator.set(true);
	}
	
	public void setDefaultRatchetPosition() {
		ratchetActuator.set(false);
	}
}
