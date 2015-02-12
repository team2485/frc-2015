package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.HandleThreshold;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Ben
 * @author Aidan
 *
 */
public class Clapper {

	private CombinedVictorSP clapperLifter;

	private DoubleSolenoid clapperActuator;

	private PIDController clapperPID;
	private AnalogPotentiometer pot;
	private boolean open;
	
	public double
		kP	= 0.01,
		kI	= 0.00,
		kD	= 0.00;
	
	private double potValue;
	
	private boolean isPID;

	private boolean automatic;
	
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

	private static final double LOWEST_POS = 0;

	private static final double POS_RANGE = 0;

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
	
//	public Clapper(int clapperActuatorPort1, int clapperActuatorPort2) {
//		this.clapperActuator = new DoubleSolenoid(clapperActuatorPort1, clapperActuatorPort2);
//	}

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
	
	public void setSetpoint(double setpoint) {
		setAutomatic();
		clapperPID.setSetpoint(setpoint);
	}

//	public void setPercentSetpoint(double setpointPercent) {
//		double correctedSetpoint = POT_MULTIPLIER * setpointPercent + POT_OFFSET;		
//		clapperPID.setSetpoint(correctedSetpoint);
//	}
	
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

	
	public double getPercentHeight() {
		return (pot.get() - LOWEST_POS)/POS_RANGE;
	}
	
	/**
	 * Sets the claw to automatic control, PID will control the winch, moveManually will not function
	 */
	public void setAutomatic() {
		automatic = true;
		clapperPID.enable();
	}
	
	/**
	 * Sets the claw to manual control, PID will not control elevation, but the moveManually method will function. 
	 */
	public void setManual() {
		automatic = false;
		clapperPID.disable();
	}

	/**
	 * Returns if the winch is being controlled by PID.
	 */
	public boolean isAutomatic() {
		return automatic;
	}
	
	/**
	 * Returns if the winch can be controlled manually.
	 */
	public boolean isManual() {
		return !automatic;
	}
	
	
	/*
	 * Assuming that a positive speed moves the clapper down
	 */
	public void moveManually(double speed){
		setManual();
		double adjustedSpeed = HandleThreshold.handleThreshold(speed, 0.1)/4;
		System.out.println(speed + " | " + adjustedSpeed);
		if (adjustedSpeed > 1){
			adjustedSpeed = 1;
		} else if (adjustedSpeed < -1){
			adjustedSpeed = -1;
		}
		
		if (isManual()){
//			if (pot.get() >= LOWEST_POS + POS_RANGE && adjustedSpeed > 0){
//				adjustedSpeed = 0;
//			}else if (pot.get() <= LOWEST_POS && adjustedSpeed < 0){
//				adjustedSpeed = 0;
//			}
			clapperLifter.set(adjustedSpeed);
		}
	}
}


	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
