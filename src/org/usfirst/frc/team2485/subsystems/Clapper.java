package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.InvertedPot;
import org.usfirst.frc.team2485.util.ThresholdHandler;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Ben Clark
 * @author Aidan Fay
 */

//TODO: find out where the PID is enabled after a sequence
public class Clapper {

	private CombinedVictorSP clapperLifter;
	private DoubleSolenoid clapperActuator;
	private PIDController clapperPID;
	private AnalogPotentiometer pot;
	private InvertedPot potInverted;
	 
	private boolean open;
	private boolean automatic;

	public double
		kP	= 0.05,
		kI	= 0.00,
		kD	= 0.00;
			
	private static final double LOWEST_POS = 500; 
	private static final double POS_RANGE = 375;
	private static final double POT_TOLERANCE = 12;
	
	public static final double 
		ABOVE_RATCHET_SETPOINT		= LOWEST_POS + 170,
		ON_RATCHET_SETPOINT			= LOWEST_POS + 125, 
		LOADING_SETPOINT			= LOWEST_POS + 10,
		COOP_ZERO_TOTE_SETPOINT		= LOWEST_POS + 77, 
		COOP_ONE_TOTE_SETPOINT		= LOWEST_POS + 175, 
		COOP_TWO_TOTES_SETPOINT		= LOWEST_POS + 275,
		COOP_THREE_TOTES_SETPOINT	= LOWEST_POS + 370, 
		SCORING_PLATFORM_HEIGHT		= LOWEST_POS + 25;
	
	private static final double LIFT_DEADBAND = 0.5;

	public Clapper(VictorSP clapperLifter1, VictorSP clapperLifter2,
			DoubleSolenoid clapperActuator, AnalogPotentiometer pot) {

		this.clapperLifter			= new CombinedVictorSP(clapperLifter1, clapperLifter2);
		this.clapperActuator		= clapperActuator;
		this.pot					= pot;
		
		this.potInverted			= new InvertedPot(pot);
		
		this.clapperPID = new PIDController(kP, kI, kD, potInverted, clapperLifter);
		this.clapperPID.setAbsoluteTolerance(POT_TOLERANCE);
		this.clapperPID.setOutputRange(-0.3, 0.4); // positive is up
		
		this.automatic				= false;
		this.open					= false;
		
		clapperLifter.invertMotorDirection(true);
	}
	
	public Clapper(int clapperLifter1Port, int clapperLifter2Port, 
			int clapperActuatorPort1, int clapperActuatorPort2, int potPort) {

		this(new VictorSP(clapperLifter1Port), new VictorSP(clapperLifter2Port),
				new DoubleSolenoid(clapperActuatorPort1, clapperActuatorPort2),
				new AnalogPotentiometer(potPort));
	}
	
	public double getPotValue() {
		return potInverted.pidGet();
	}
	
	public void setPID(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		
		clapperPID.setPID(kP, kI, kD);
	}
	
	public double getkP() {
		return kP;	
	}

	public void setkP(double kP) {
		this.kP = kP;
		clapperPID.setPID(kP, kI, kD);
	}

	public double getkI() {
		return kI;
	}

	public void setkI(double kI) {
		this.kI = kI;
		clapperPID.setPID(kP, kI, kD);
	}

	public double getkD() {
		return kD;
	}

	public void setkD(double kD) {
		this.kD = kD;
		clapperPID.setPID(kP, kI, kD);
	}

	public void setSetpoint(double setpoint) {
		setAutomatic();
		clapperPID.setSetpoint(setpoint);
	}
	
	public double getSetpoint() {
		return clapperPID.getSetpoint();
	}
	
	public boolean isPIDOnTarget() {
		return clapperPID.onTarget(); 
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

	
	public double getPercentHeight() {
		return (potInverted.pidGet() - LOWEST_POS)/POS_RANGE;
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
	public void liftManually(double speed) {
		speed/=3;
		setManual();
		
		if (potInverted.pidGet() < LOWEST_POS || potInverted.pidGet() > LOWEST_POS + POS_RANGE) 
			return; 
		
		//double adjustedSpeed = ThresholdHandler.handleThreshold(speed, LIFT_DEADBAND)/2;
		if (speed > 1){
			speed = 1;
		} else if (speed < -1){
			speed = -1;
		}
		
		//System.out.println("in lift manually, adjustSpeed is " + adjustedSpeed);
		clapperLifter.set(speed);
		
//		System.out.println(speed + " | " + adjustedSpeed);

	}

	public double getMotorOutput() {
		return clapperPID.get(); 
	}

	public double getError() {
		return clapperPID.getError();
	}
}

	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
