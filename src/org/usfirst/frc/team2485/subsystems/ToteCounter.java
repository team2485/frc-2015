package org.usfirst.frc.team2485.subsystems;

 
/**
 * 
 * Used to keep track of the current number of totes the robot is holding. 
 * 
 * Tote count effects stregnth of kP in PID loops for lifting the Clapper and Claw during automated sequences.
 * Tote count also acts as a safety. Automated Sequences will only run if the current tote count logically makes sense. 
 * 
 * @see SequencerFactory
 * @see Clapper
 * @see Claw
 * 
 * @author Ben Clark
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
