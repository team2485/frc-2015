package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.util.CombinedVictorSP;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Clapper {

	private CombinedVictorSP clapperLifter;

	private DoubleSolenoid clapperActuator;

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
	
	public static final double 
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
			DoubleSolenoid clapperActuator, AnalogPotentiometer pot) {

		this.clapperLifter			= new CombinedVictorSP(clapperLifter1, clapperLifter2);
		this.clapperActuator		= clapperActuator;
		this.pot					= pot;
		
		this.clapperPID = new PIDController(kP, kI, kD, pot, clapperLifter);
	}

	public Clapper(int clapperLifter1Port, int clapperLifter2Port, 
			int clapperActuatorPort1, int clapperActuatorPort2, int potPort) {

		this(new VictorSP(clapperLifter1Port), new VictorSP(clapperLifter2Port),
				new DoubleSolenoid(clapperActuatorPort1, clapperActuatorPort2),
				new AnalogPotentiometer(potPort));
	}
	
	public Clapper(int clapperActuatorPort1, int clapperActuatorPort2) {
		this.clapperActuator = new DoubleSolenoid(clapperActuatorPort1, clapperActuatorPort2);
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
	
	public void setRawSetpoint(double setpoint) {
		
		clapperPID.setSetpoint(setpoint);
	}

	public void setPercentSetpoint(double setpointPercent) {
		double correctedSetpoint = POT_MULTIPLIER * setpointPercent + POT_OFFSET;		
		clapperPID.setSetpoint(correctedSetpoint);
	}
	
	public double getPercentHeight() {
		return (pot.get() - POT_OFFSET) / POT_MULTIPLIER;
	}
	
	public void openClapper() {
		clapperActuator.set(DoubleSolenoid.Value.kForward);
		open = true;
	}

	public void closeClapper() {
		clapperActuator.set(DoubleSolenoid.Value.kReverse);
		open = false;
	}

	public boolean isOpen() {
		return open;
	}



	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
}