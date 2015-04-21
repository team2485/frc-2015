package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.util.CombinedSpeedController;
import org.usfirst.frc.team2485.util.InvertedScaledPot;
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
 * @author Anoushka Bose
 */

public class Clapper {

	private CombinedSpeedController clapperLifter;
	private Solenoid clapperActuator;
	public PIDController clapperPID;
	private ScaledPot potScaled;
	private DigitalInput toteDetectorLimitSwitch, bottomSafetyLimitSwitch;

	private boolean automatic;
	private double lastHeight;

	public static final double 	LOWEST_POS 	= 207, 
								HIGHEST_POS	= 525;	
	
	private static final double POT_RANGE = HIGHEST_POS - LOWEST_POS; 
	
	public static final double POT_TOLERANCE = 18;
	
	public static final double INCH_LOW_POS	 = 6.125;
	private static final double INCH_RANGE	 = 36.25; 
	
	@SuppressWarnings("unused")
	public static final double POTS_PER_INCH = POT_RANGE/INCH_RANGE;
	
	
	private double pidOutputMin, pidOutputMinNormal = -0.2, pidOutputMax, pidOutputMaxNormal = 0.5;
	
	public static final double 
		kP_1_TOTES_UP = 0.02, 
		kP_2_TOTES_UP = 0.025,
		kP_3_TOTES_UP = 0.035,
		kP_4_TOTES_UP = 0.045,
		kP_5_TOTES_UP = 0.06; 
	
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
		RIGHTING_CONTAINER_POS									= LOWEST_POS + 140, 
		RIGHTING_CONTAINER_PRE_POS								= LOWEST_POS + 3, // lowest
		ABOVE_RATCHET_SETPOINT									= LOWEST_POS + 160, // AB 4/2
		DROP_OFF_POS_ON_ONE_TOTE								= ABOVE_RATCHET_SETPOINT,
		ON_RATCHET_SETPOINT										= LOWEST_POS + ((125/800.0)*POT_RANGE),  
		HOLDING_TOTE_SETPOINT									= LOWEST_POS + 95, // AB 4/2
		LOADING_SETPOINT										= LOWEST_POS + 2, // AB 4/2
//		COOP_ZERO_TOTE_SETPOINT									= LOWEST_POS + 86, 
//		COOP_ONE_TOTE_SETPOINT									= LOWEST_POS + ((175/800.0)*POT_RANGE), 
//		COOP_TWO_TOTES_SETPOINT									= LOWEST_POS + ((275/800.0)*POT_RANGE),
//		COOP_THREE_TOTES_SETPOINT								= LOWEST_POS + ((370/800.0)*POT_RANGE), 
		SCORING_PLATFORM_HEIGHT									= LOWEST_POS + ((25/800.0)*POT_RANGE),
		LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT	= LOWEST_POS + 50, // AB 4/2
//		FIX_CONTAINER_IN_CLAW_POS								= LOWEST_POS + ((125/800.0)*POT_RANGE),
		ABOVE_STEP_SETPOINT 									= LOWEST_POS + 96,
		TOTE_HEIGHT 											= 11.5 * POTS_PER_INCH; // AB 4/2
	
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
		
		this.toteDetectorLimitSwitch = toteDetectorLimitSwitch;
		this.bottomSafetyLimitSwitch = bottomSafetyLimitSwitch;
		
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
		lastHeight = getPotValue(); 
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
		clapperActuator.set(false);
	}

	public void closeClapper() {
		clapperActuator.set(true);
	}

	public boolean isOpen() {
		return !clapperActuator.get();
	}
	
	public double getPercentHeight() {
		return (potScaled.pidGet() - LOWEST_POS)/POT_RANGE;
	}
	
	public double getInchHeight() {
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
	
	public void liftManually(double speed) {
		
		setManual();
		
		if (speed > 1)
			speed = 1;
		else if (speed < -1)
			speed = -1;
		
		
		System.out.println("in lift manually, adjustSpeed is " + speed);
		clapperLifter.set(speed);
	}

	public double getMotorOutput() {
		return clapperPID.get(); 
	}

	public double getError() {
		return clapperPID.getError();
	}
	
	public boolean toteDetected() {
		return !toteDetectorLimitSwitch.get(); 
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
		
		if (pidOutputMin > 0)
			pidOutputMin = 0.0;
		pidOutputMax = pidOutputMaxNormal + .05 * toteCount;
		
		clapperPID.setOutputRange(pidOutputMin, pidOutputMax);
	}

	public boolean isMoving() {
		return clapperLifter.isMoving(); 
	}

	public void enablePID() {
		clapperPID.enable();
	}
}