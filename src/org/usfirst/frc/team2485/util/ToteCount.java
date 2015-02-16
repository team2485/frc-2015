package org.usfirst.frc.team2485.util;
 
/**
  * @author Ben Clark
  */
public class ToteCount {
	
	private int numTotes;
	
	public ToteCount(int totes) {
		numTotes = totes;
	}
	
	public ToteCount() {
		numTotes = 0;
	}
	
	public void addTote() {
		numTotes = numTotes >= 6 ? 6 : numTotes + 1;
	}
	
	public void addTote(int totes) {
		numTotes = numTotes >= 6 ? 6 : numTotes + totes;
	}
	
	public void reset() {
		numTotes = 0;
	}
	
	public int get() {
		return numTotes;
	}
}
