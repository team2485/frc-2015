package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

public class ContainerCommandeerer {

	private Solenoid	solRight, solLeft;
	private boolean		rightLiberated = false, leftLiberated = false;

	public ContainerCommandeerer(Solenoid solRight, Solenoid solLeft) {
		this.solRight = solRight;
		this.solLeft  = solLeft;
	}

	public void liberateRight() {
		solRight.set(true);
		rightLiberated = true;
	}

	public boolean isRightLiberated() {
		return rightLiberated;
	}

	public void liberateLeft() {
		solLeft.set(true);
		leftLiberated = true;
	}

	public boolean isLeftLiberated() {
		return leftLiberated;
	}

	public void resetSol() {
		solRight.set(false);
		solLeft.set(false);
	}

}
