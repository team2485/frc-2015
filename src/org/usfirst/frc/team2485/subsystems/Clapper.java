package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.InvertedPot;
import org.usfirst.frc.team2485.util.ScaledPot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Ben Clark
 * @author Aidan Fay
 * @author Patrick Wamsley
 */

public class Clapper {

	private CombinedVictorSP clapperLifter;
	private DoubleSolenoid clapperActuator;
	public PIDController clapperPID;
//	private AnalogPotentiometer pot;
	private ScaledPot potScaled;
	 
	private boolean open;
	private boolean automatic;

	public double
		kP	= 0.05,
		kI	= 0.00,
		kD	= 0.00;
	
	public static final double //these are not tested at all whatsoever
		kP_1_TOTES_UP = 0.05,
		kP_2_TOTES_UP = 0.055,
		kP_3_TOTES_UP = 0.06,
		kP_4_TOTES_UP = 0.065,
		kP_5_TOTES_UP = 0.07,
		kP_6_TOTES_UP = 0.75;
	public static final double //these are not tested at all whatsoever
		kP_1_TOTES_DOWN = 0.05,
		kP_2_TOTES_DOWN = 0.05,
		kP_3_TOTES_DOWN = 0.05,
		kP_4_TOTES_DOWN = 0.04,
		kP_5_TOTES_DOWN = 0.03,
		kP_6_TOTES_DOWN = 0.02;
										
	public static final double LOWEST_POS = 84; 	
	private static final double POS_RANGE = 375;
	public static final double POT_TOLERANCE = 18;
	private static final double INCH_RANGE  = 44.375; // 6 and 1/8 in from floor (corresponds to a pot value of 84) - 50.5 in
	 
	private DigitalInput toteDetectorLimitSwitch, bottomSafetyLimitSwitch;
	
	private double lastHeight;
	
	public static final double 
		ABOVE_RATCHET_SETPOINT									= LOWEST_POS + 170,
		DROP_OFF_POS_ON_ONE_TOTE	= ABOVE_RATCHET_SETPOINT,
		ON_RATCHET_SETPOINT			= LOWEST_POS + 125, 
		HOLDING_TOTE_SETPOINT		= LOWEST_POS + 100, //TODO: find value
		LOADING_SETPOINT			= LOWEST_POS + 2,
		COOP_ZERO_TOTE_SETPOINT		= LOWEST_POS + 77, 
		COOP_ONE_TOTE_SETPOINT		= LOWEST_POS + 175, 
		COOP_TWO_TOTES_SETPOINT		= LOWEST_POS + 275,
		COOP_THREE_TOTES_SETPOINT	= LOWEST_POS + 370, 
		SCORING_PLATFORM_HEIGHT		= LOWEST_POS + 25,
		LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT	= LOWEST_POS + 50;
	
	private static final double LIFT_DEADBAND = 0.5;
	private double pidOutputMin, pidOutputMinNormal = -0.2, pidOutputMax, pidOutputMaxNormal = 0.5;

	public Clapper(VictorSP clapperLifter1, VictorSP clapperLifter2,
			DoubleSolenoid clapperActuator, AnalogPotentiometer pot, 
			DigitalInput toteDetectorLimitSwitch, DigitalInput bottomSafetyLimitSwitch) {

		this.clapperLifter			= new CombinedVictorSP(clapperLifter1, clapperLifter2);
		this.clapperActuator		= clapperActuator;
//		this.pot					= pot;
		
		this.potScaled				= new ScaledPot(pot);
		
		this.clapperPID = new PIDController(kP, kI, kD, potScaled, clapperLifter);
		this.clapperPID.setAbsoluteTolerance(POT_TOLERANCE);
		pidOutputMin = pidOutputMinNormal;
		pidOutputMax = pidOutputMaxNormal;
		this.clapperPID.setOutputRange(pidOutputMin, pidOutputMax); // positive is up
		
		this.automatic				= false;
		this.open					= false;
		
		this.toteDetectorLimitSwitch = toteDetectorLimitSwitch;
		this.bottomSafetyLimitSwitch = bottomSafetyLimitSwitch;
		//clapperLifter.invertMotorDirection(true);
		
		lastHeight = getPotValue(); 
	}
	
	public Clapper(int clapperLifter1Port, int clapperLifter2Port, 
			int clapperActuatorPort1, int clapperActuatorPort2, int potPort, int detectorswitchport, int safetyswitchport) {

		this(new VictorSP(clapperLifter1Port), new VictorSP(clapperLifter2Port),
				new DoubleSolenoid(clapperActuatorPort1, clapperActuatorPort2),
				new AnalogPotentiometer(potPort), new DigitalInput(detectorswitchport), new DigitalInput(safetyswitchport));
	} 
	
	public double getChangeInHeightInInches() {
		return ((potScaled.pidGet() - lastHeight)/(POS_RANGE))*INCH_RANGE; 
	}
	
	public void updateLastHeight() {
		lastHeight = getPotValue(); //need to map to inches?  
	}
	public double getPotValue() {
		return potScaled.pidGet();
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
		return (potScaled.pidGet() - LOWEST_POS)/POS_RANGE;
	}
	
	public double getInchHeight() {
		// TODO: Test
		return(potScaled.pidGet() - LOWEST_POS)/(POS_RANGE)*INCH_RANGE + 6.125;
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
	 * Returns true if the winch is being controlled by PID.
	 */
	public boolean isAutomatic() {
		return automatic;
	}
	
	/**
	 * Returns true if the winch can be controlled manually.
	 */
	public boolean isManual() {
		return !automatic;
	}
	
	/*
	 * Assuming that a positive speed moves the clapper down
	 */
	public void liftManually(double speed) {
		
		setManual();
		
//		if ((potScaled.pidGet() < LOWEST_POS  && speed > 0) || (potScaled.pidGet() > LOWEST_POS + POS_RANGE && speed < 0))
//			return; 
		
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

	public void checkSafety() {
		if (bottomSafetyLimitSwitch.get())
			clapperPID.setOutputRange(0.0, pidOutputMax);
		else
			clapperPID.setOutputRange(pidOutputMin, pidOutputMax);
	}
	
	public void setKP(double kP) {
		this.kP = kP; 
	}
	
	public boolean toteDetected() {
		if(toteDetectorLimitSwitch.get())
			System.out.println("tote not detected");
		else
			System.out.println("tote detected");
		return !(toteDetectorLimitSwitch.get()); 
	}
	
	public void updateToteCount( int toteCount )
	{
		if(toteCount == 1)
			setPID(kP_1_TOTES_UP, kI, kD);
		else if(toteCount == 2)
			setPID(kP_2_TOTES_UP, kI, kD);
		else if(toteCount == 3)
			setPID(kP_3_TOTES_UP, kI, kD);
		else if(toteCount == 4)
			setPID(kP_4_TOTES_UP, kI, kD);
		else if(toteCount == 5)
			setPID(kP_5_TOTES_UP, kI, kD);
		
		pidOutputMin = pidOutputMinNormal + .02*toteCount;
		pidOutputMax = pidOutputMaxNormal + .05*toteCount;
		this.clapperPID.setOutputRange(pidOutputMin, pidOutputMax);
	}
}

	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
