package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

/**
 * @author Aidan Fay
 */

public class CommandeerContainerSequence implements SequencedItem {
	
	private int	direction;
	public static final int LEFT = 0, RIGHT = 1, BOTH = 2, RETRACT_BOTH = 3;

	public CommandeerContainerSequence(int direction) {
		this.direction = direction;
	}

	@Override
	public void run() {
		switch (direction) {
		
			case LEFT: 
				Robot.containerCommandeerer.liberateLeft();				
				break;
				
			case RIGHT:
				Robot.containerCommandeerer.liberateRight();
				break;
				
			case BOTH:
				Robot.containerCommandeerer.liberateLeft();
				Robot.containerCommandeerer.liberateRight();
				break;
				
			case RETRACT_BOTH:
				Robot.containerCommandeerer.resetSol();
				break;

			default:
				throw new IllegalArgumentException(); 
		}
	}

	@Override
	public double duration() {
		return 0.1;
	}

}
