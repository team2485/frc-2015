package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

public class ContainerCommandeerer {

	private Solenoid solRight, solLeft;
	
//	private boolean	rightLiberated = false, leftLiberated = false;	//redundant...can query the solenoids for this data

	public ContainerCommandeerer(Solenoid solRight, Solenoid solLeft) {
		this.solRight = solRight;
		this.solLeft  = solLeft;
	}

	public void liberateRight() {
		solRight.set(true);
	}

	public boolean isRightLiberated() {
		return solRight.get();
	}

	public void liberateLeft() {
		solLeft.set(true);
	}

	public boolean isLeftLiberated() {
		return solLeft.get();
	}

	public void resetSol() {
		solRight.set(false);
		solLeft.set(false);
	}

}
