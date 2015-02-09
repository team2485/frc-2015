package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

public class RatchetSystem {

	private Solenoid hook, bottomRest;
	
	public RatchetSystem(Solenoid hook, Solenoid bottomRest) {
		this.hook		= hook;
		this.bottomRest	= bottomRest;
	}
	
	public RatchetSystem(int hookPort, int bottomRestPort) {
		this(new Solenoid(hookPort), new Solenoid(bottomRestPort));
	}
	
	public void releaseToteStack() {
		hook.set(true);
		bottomRest.set(true);
	}
	
	public void setDefaultRatchetPosition() {
		hook.set(false);
		bottomRest.set(false);
	}
}
