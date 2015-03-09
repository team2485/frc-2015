package org.usfirst.frc.team2485.util;
 
/**
  * @author Ben Clark
  * @author Patrick Wamsley
  */
public class ToteCounter {
	
	private int numTotes;
	
	public ToteCounter(int totes) {
		numTotes = totes;
	}
	
	public ToteCounter() {
		numTotes = 0;
	}
	
	public void addTote() {
		numTotes = numTotes >= 5 ? 5 : numTotes + 1;
	}
	
	public void subtractTote() {
		numTotes = numTotes <= 0 ? 0 : numTotes - 1;
	}
	
	public void addTote(int totes) {
		numTotes = numTotes >= 5 ? 5 : numTotes + totes;
	}
	
	public void resetCount() {
		numTotes = 0;
	}
	
	public int getCount() {
		return numTotes;
	}
}
