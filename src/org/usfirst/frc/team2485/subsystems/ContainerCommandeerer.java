package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * @author Aidan Fay
 */

public class ContainerCommandeerer {

	private Solenoid solRight, solLeft;

	public ContainerCommandeerer(Solenoid solRight, Solenoid solLeft) {
		this.solRight = solRight;
		this.solLeft  = solLeft;
	}

	public void liberateRight() {
		solRight.set(true);
	}

	public void liberateLeft() {
		solLeft.set(true);
	}

	public void resetSol() {
		solRight.set(false);
		solLeft.set(false);
	}

}
