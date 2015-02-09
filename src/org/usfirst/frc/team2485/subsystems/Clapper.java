package org.usfirst.frc.team2485.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Clapper {

	private VictorSP clapperLifter1, clapperLifter2;	

	private Solenoid leftClapperActuator, rightClapperActuator;

	private PIDController clapperPID;
	private AnalogPotentiometer pot;
	private static final double POT_MULTIPLIER = 0;
	private static final double POT_OFFSET = 0;
	private boolean open;
	
	public double
		kP	= 0.01,
		kI	= 0.00,
		kD	= 0.00;
	
	private double potValue;
	
	private boolean isPID;
	
	public static final int 
		ONE_TOTE_SETPOINT		= 0,
		TWO_TOTE_SETPOINT		= 0,
		THREE_TOTE_SETPOINT		= 0,
		FOUR_TOTE_SETPOINT		= 0,
		FIVE_TOTE_SETPOINT		= 0,
		SIX_TOTE_SETPOINT		= 0,
		STEP_HEIGHT				= 0,
		RATCHET_HEIGHT			= 0,
		SCORING_PLATFORM_HEIGHT	= 0;

	public Clapper(VictorSP clapperLifter1, VictorSP clapperLifter2,
			Solenoid leftClapperActuator, Solenoid rightClapperActuator,
			AnalogPotentiometer pot) {

		this.clapperLifter1			= clapperLifter1;
		this.clapperLifter2			= clapperLifter2;
		this.leftClapperActuator	= leftClapperActuator;
		this.rightClapperActuator	= rightClapperActuator;
		this.pot					= pot;
	}

	public Clapper(int clapperLifter1Port, int clapperLifter2Port, 
			int clapperActuator1Port, int clapperActuator2Port, int potPort) {

		this(new VictorSP(clapperLifter1Port), new VictorSP(clapperLifter2Port),
				new Solenoid(clapperActuator1Port), new Solenoid(clapperActuator2Port),
				new AnalogPotentiometer(potPort));
	}

	public double getPotValue() {
		return pot.get();
	}
	
	/*public double getPotPercentage() {
		return 100 * (pot.get() + POT_OFFSET);
	}*/
	
	public void setPID(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		
		clapperPID.setPID(kP, kI, kD);
	}
	
	public void setRawSetpoint(int setpoint) {
		
		switch(setpoint) {
		//different cases
		default: 
			throw new IllegalArgumentException("Not given valid setpoint"); 
		}
//		clapperPID.setSetpoint(setpoint);
	}

	public void setPercentSetpoint(double setpointPercent) {
		double correctedSetpoint = POT_MULTIPLIER * setpointPercent + POT_OFFSET;		
		clapperPID.setSetpoint(correctedSetpoint);
	}
	
	public double getPercentHeight() {
		return (pot.get() - POT_OFFSET) / POT_MULTIPLIER;
	}
	
	public void openClapper() {
		leftClapperActuator.set(true);
		rightClapperActuator.set(true);
		open = true;
	}

	public void closeClapper() {
		leftClapperActuator.set(false);
		rightClapperActuator.set(false);
		open = false;
	}
	
	public boolean isOpen() {
		return open;
	}



	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
}