package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;
import org.usfirst.frc.team2485.robot.Robot;

public class DisableStrafePID implements SequencedItem {

	@Override
	public void run() {
		Robot.drive.disableStrafePID();
		Robot.drive.disableIMUPID();
		Robot.drive.disableSonicStrafePID(); 
	}
	
	@Override
	public double duration() {
		return 0.05; 
	}
}
