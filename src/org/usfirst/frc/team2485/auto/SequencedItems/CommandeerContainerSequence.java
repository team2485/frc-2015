package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class CommandeerContainerSequence implements SequencedItem{
	
	private int	direction;
	public static final int LEFT = 0, RIGHT = 1, BOTH = 2;

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
				Robot.containerCommandeerer.liberateLeft ();
				Robot.containerCommandeerer.liberateRight();
				break;

			default:
				Robot.containerCommandeerer.liberateLeft ();
				Robot.containerCommandeerer.liberateRight();
				break;
		}
		
		
	}

	@Override
	public double duration() {
		return 0.1;//TODO find actual value
	}

}
