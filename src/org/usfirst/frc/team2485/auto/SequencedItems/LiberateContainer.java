package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class LiberateContainer implements SequencedItem{
	
	
	
	private int	direction;
	public static final int LEFT = 0, RIGHT = 1, BOTH = 2;

	public LiberateContainer(int direction) {
		this.direction = direction;
	}

	@Override
	public void run() {
		switch (direction) {
			case LEFT:
				Robot.containerLiberator.liberateLeft();
				break;
				
			case RIGHT:
				Robot.containerLiberator.liberateRight();
				break;
				
			case BOTH:
				Robot.containerLiberator.liberateLeft ();
				Robot.containerLiberator.liberateRight();
				break;

			default:
				Robot.containerLiberator.liberateLeft ();
				Robot.containerLiberator.liberateRight();
				break;
		}
		Robot.containerLiberator.liberateLeft();
		
	}

	@Override
	public double duration() {
		return 1.5;//TODO find actual value
	}

}
