package org.usfirst.frc.team2485.auto.SequencedItems;

import org.usfirst.frc.team2485.auto.SequencedItem;

public class AutoTestPrint implements SequencedItem {

	private boolean finished = false; 
	private static int numMade = 0; 
	
	public AutoTestPrint() {
		numMade++; 
	}
	
	@Override
	public void run() {
		System.out.println("Testing auto... run method called, numMade = " + numMade);
		finished = true; 
	}

	@Override
	public double duration() {
		return finished ? 0 : 1;
	}

}
