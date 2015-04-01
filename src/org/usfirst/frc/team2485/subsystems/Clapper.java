package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.util.CombinedSpeedController;
import org.usfirst.frc.team2485.util.InvertedPot;
import org.usfirst.frc.team2485.util.ScaledPot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Ben Clark
 * @author Aidan Fay
 * @author Patrick Wamsley
 */

public class Clapper {

	private CombinedSpeedController clapperLifter;
	private Solenoid clapperActuator;
	public PIDController clapperPID;
	private ScaledPot potScaled;
	private DigitalInput toteDetectorLimitSwitch, bottomSafetyLimitSwitch;

	private boolean open;
	private boolean automatic;
	private double lastHeight;

	public static final double LOWEST_POS = 85; 	// NEED TO CHECK ON VALKYRIE
	public static final double HIGHEST_POS = 883;	// NEEDS TO CHECK ON VALKYRIE 
	private static final double POT_RANGE = HIGHEST_POS - LOWEST_POS; 
	public static final double POT_TOLERANCE = 18;
	public static final double INCH_LOW_POS = 6.125;
	private static final double INCH_RANGE  = 38.875; // 6 and 1/8 in from floor to the top which is 45 in
	@SuppressWarnings("unused")
	private static final double POTS_PER_INCH = POT_RANGE/INCH_RANGE;
	
//	private static final double LIFT_DEADBAND = 0.5;
	
	private double pidOutputMin, pidOutputMinNormal = -0.2, pidOutputMax, pidOutputMaxNormal = 0.5;
	
	public static final double 
		kP_1_TOTES_UP = 0.01,	
		kP_2_TOTES_UP = 0.012,
		kP_3_TOTES_UP = 0.015,
		kP_4_TOTES_UP = 0.035,
		kP_5_TOTES_UP = 0.05; //0.4
	
	public static final double
		kP_DEFAULT	= kP_1_TOTES_UP, 
		kI_DEFAULT	= 0.00,
		kD_DEFAULT	= 0.00;
	
	/* unused
	public static final double 
		kP_1_TOTES_DOWN = 0.05,
		kP_2_TOTES_DOWN = 0.05,
		kP_3_TOTES_DOWN = 0.05,
		kP_4_TOTES_DOWN = 0.04,
		kP_5_TOTES_DOWN = 0.03;
	*/	
	
	public static final double 
		RIGHTING_CONTAINER_POS									= LOWEST_POS + 335, 
		RIGHTING_CONTAINER_PRE_POS								= LOWEST_POS + 58, 
		ABOVE_RATCHET_SETPOINT									= LOWEST_POS + 333, // 430, changed from 335 on 20 march because mechanical // UPDATE changed to 299 on 3/22/15
		DROP_OFF_POS_ON_ONE_TOTE								= ABOVE_RATCHET_SETPOINT,
		ON_RATCHET_SETPOINT										= LOWEST_POS + 125, //changed from 125  
		HOLDING_TOTE_SETPOINT									= LOWEST_POS + 235, // 387, changed from 262 on 20 mar bc mechanical
		LOADING_SETPOINT										= LOWEST_POS + 5,
		COOP_ZERO_TOTE_SETPOINT									= LOWEST_POS + 77, 
		COOP_ONE_TOTE_SETPOINT									= LOWEST_POS + 175, 
		COOP_TWO_TOTES_SETPOINT									= LOWEST_POS + 275,
		COOP_THREE_TOTES_SETPOINT								= LOWEST_POS + 370, 
		SCORING_PLATFORM_HEIGHT									= LOWEST_POS + 25,
		LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT	= LOWEST_POS + 100,
		FIX_CONTAINER_IN_CLAW_POS								= LOWEST_POS + 125,
		ABOVE_STEP_SETPOINT 									= LOWEST_POS + 215,
		TOTE_HEIGHT 											= 255;
	
	public Clapper(CombinedSpeedController clapperLifter, Solenoid clapperActuator2, AnalogPotentiometer pot, 
			DigitalInput toteDetectorLimitSwitch, DigitalInput bottomSafetyLimitSwitch) {

		this.clapperLifter			= clapperLifter; 
		this.clapperActuator		= clapperActuator2;
		
		this.potScaled				= new ScaledPot(pot);
		
		this.clapperPID = new PIDController(kP_DEFAULT, kI_DEFAULT, kD_DEFAULT, potScaled, clapperLifter);
		this.clapperPID.setAbsoluteTolerance(POT_TOLERANCE);
		
		pidOutputMin = pidOutputMinNormal;
		pidOutputMax = pidOutputMaxNormal;
		
		this.clapperPID.setOutputRange(pidOutputMin, pidOutputMax); // positive is up
		
		this.automatic				= false;
		this.open					= true;
		
		this.toteDetectorLimitSwitch = toteDetectorLimitSwitch;
		this.bottomSafetyLimitSwitch = bottomSafetyLimitSwitch;
		//clapperLifter.invertMotorDirection(true);
		
		lastHeight = getPotValue(); 
	}
	
	public Clapper(CombinedSpeedController clapperLifter, AnalogPotentiometer pot) {

		this.clapperLifter			= clapperLifter; 		
		this.potScaled				= new ScaledPot(pot);
		
		this.clapperPID = new PIDController(kP_DEFAULT, kI_DEFAULT, kD_DEFAULT, potScaled, clapperLifter);
		this.clapperPID.setAbsoluteTolerance(POT_TOLERANCE);
		
		pidOutputMin = pidOutputMinNormal;
		pidOutputMax = pidOutputMaxNormal;
		
		this.clapperPID.setOutputRange(pidOutputMin, pidOutputMax); // positive is up
		
		lastHeight = getPotValue(); 
	}
	
	public Clapper(int clapperLifter1Port, int clapperLifter2Port, 
			int clapperActuatorPort1, int clapperActuatorPort2, int potPort, int detectorswitchport, int safetyswitchport) {

		this(new CombinedSpeedController(new VictorSP(clapperLifter1Port), new VictorSP(clapperLifter2Port)),
				new Solenoid(clapperActuatorPort1,clapperActuatorPort2),
				new AnalogPotentiometer(potPort), new DigitalInput(detectorswitchport), new DigitalInput(safetyswitchport));
	} 
	
	public double getChangeInHeightInInches() {
		return ((potScaled.pidGet() - lastHeight) / (POT_RANGE)) * INCH_RANGE; 
	}
	
	public void updateLastHeight() {
		lastHeight = getPotValue(); //need to map to inches?  
	}
	public double getPotValue() {
		return potScaled.pidGet();
	}
	
	public void setPID(double kP, double kI, double kD) {
		clapperPID.setPID(kP, kI, kD);
	}
	
	public double getkP() {
		return clapperPID.getP();	
	}

	public void setkP(double kP) {
		clapperPID.setPID(kP, clapperPID.getI(), clapperPID.getD());
	}

	public double getkI() {
		return clapperPID.getI();
	}

	public void setkI(double kI) {
		clapperPID.setPID(clapperPID.getP(), kI, clapperPID.getD());
	}

	public double getkD() {
		return clapperPID.getD();
	}

	public void setkD(double kD) {
		clapperPID.setPID(clapperPID.getP(), clapperPID.getI(), kD);
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
		clapperActuator.set(true);
//		clapperActuator.set(DoubleSolenoid.Value.kReverse);
		open = true;
	}

	public void closeClapper() {
		clapperActuator.set(false);
//		clapperActuator.set(DoubleSolenoid.Value.kForward);
		open = false;
	}

	public boolean isOpen() {
		return clapperActuator.get();
	}
	
	public double getPercentHeight() {
		return (potScaled.pidGet() - LOWEST_POS)/POT_RANGE;
	}
	
	public double getInchHeight() {
		// TODO: Test
		return(potScaled.pidGet() - LOWEST_POS) / (POT_RANGE) * INCH_RANGE + INCH_LOW_POS;
	}
	
	public double getPotValueFromInchHeight(double inches) {
		double inchesAboveLowPos = inches - INCH_LOW_POS;
		return inchesAboveLowPos/INCH_RANGE * POT_RANGE + LOWEST_POS;
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
		if (speed > 1)
			speed = 1;
		else if (speed < -1)
			speed = -1;
		
		
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
	
	public boolean toteDetected() {
		if(!toteDetectorLimitSwitch.get())
			System.out.println("tote detected");
//		else
//			System.out.println("tote detected");
		return !(toteDetectorLimitSwitch.get()); 
	}
	
	public void updateToteCount(int toteCount) {
		if (toteCount == 1)
			setPID(kP_1_TOTES_UP, kI_DEFAULT, kD_DEFAULT);
		else if (toteCount == 2)
			setPID(kP_2_TOTES_UP, kI_DEFAULT, kD_DEFAULT);
		else if (toteCount == 3)
			setPID(kP_3_TOTES_UP, kI_DEFAULT, kD_DEFAULT);
		else if (toteCount == 4)
			setPID(kP_4_TOTES_UP, kI_DEFAULT, kD_DEFAULT);
		else if (toteCount == 5)
			setPID(kP_5_TOTES_UP, kI_DEFAULT, kD_DEFAULT);
		
		pidOutputMin = pidOutputMinNormal + .02 * toteCount;
		pidOutputMax = pidOutputMaxNormal + .05 * toteCount;
		this.clapperPID.setOutputRange(pidOutputMin, pidOutputMax);
	}

	public boolean isMoving() {
		return clapperLifter.isMoving(); 
	}

	public void enablePID() {
		clapperPID.enable();
	}
}

	//  two belts for intake, pneumatic for finger, pneumatic for opens and closes whole intake, one pneumatic for open/closes 
	//	the belts, sensors for detecting tote
