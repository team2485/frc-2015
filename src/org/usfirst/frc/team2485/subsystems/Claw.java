package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.DummyOutput;
import org.usfirst.frc.team2485.util.ScaledPot;
import org.usfirst.frc.team2485.util.ThresholdHandler;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Aidan Fay
 * @author Ben Clark
 */

public class Claw {
	
	private double kP = 0.0025, kI = 0, kD = 0;
	private CombinedVictorSP winchMotor;
	private Solenoid actuator;
	private ScaledPot potScaled;
	private DummyOutput dummyWinch;

	private static final double LOWEST_POS = 110; 	// top: 850 bottom: 112
	private static final double POS_RANGE = 740;
	public static final double POT_TOLERANCE = 12;
	private static final double INCH_RANGE  = 63.75; // 11.25 in from floor (corresponds to a pot value of LOWEST_POS) - 75 in
	
	private static final double LOADING_RESTING_OFFSET = 50; 
	
	public static final double 
		CONTAINER_LOADING_POINT	= LOWEST_POS,
		HIGHEST_POS				= LOWEST_POS + POS_RANGE - 5,
		ONE_TOTE_RESTING		= LOWEST_POS + 370,
		ONE_TOTE_LOADING		= ONE_TOTE_RESTING + LOADING_RESTING_OFFSET, // 469
		TWO_TOTE_RESTING		= LOWEST_POS + 340,
		TWO_TOTE_LOADING		= TWO_TOTE_RESTING + LOADING_RESTING_OFFSET,
		THREE_TOTE_RESTING		= LOWEST_POS + 340,
		THREE_TOTE_LOADING		= THREE_TOTE_RESTING + LOADING_RESTING_OFFSET,
		FOUR_TOTE_RESTING		= LOWEST_POS + 340,
		FOUR_TOTE_LOADING		= FOUR_TOTE_RESTING + LOADING_RESTING_OFFSET,
		FIVE_TOTE_RESTING		= LOWEST_POS + 340,
		FIVE_TOTE_LOADING		= FIVE_TOTE_RESTING + LOADING_RESTING_OFFSET,
		SIX_TOTE_RESTING		= LOWEST_POS + 340,
		SIX_TOTE_LOADING		= SIX_TOTE_RESTING + LOADING_RESTING_OFFSET; 
	
	public static final double 	PLACE_ON_EXISTING_STACK_THREE_TOTES = LOWEST_POS + 455,
								PLACE_ON_EXISTING_STACK_FOUR_TOTES = LOWEST_POS + 600,
								PLACE_ON_EXISTING_STACK_FIVE_TOTES = LOWEST_POS + 750,
								PLACE_ON_EXISTING_STACK_SIX_TOTES = LOWEST_POS + POS_RANGE;
	
	// top of the claw: 870
	// bottom of the claw: 118
	
	private PIDController elevationPID;
	
	private boolean automatic = true;

	public Claw(VictorSP winchMotor, Solenoid actuator, AnalogPotentiometer pot) {
		this.winchMotor = new CombinedVictorSP(winchMotor);
		this.winchMotor.invertMotorDirection(true);
		this.actuator 	= actuator;
		this.potScaled	= new ScaledPot(pot);
		this.dummyWinch = new DummyOutput();
		
		elevationPID = new PIDController(kP, kI, kD, potScaled, this.dummyWinch);
		elevationPID.setAbsoluteTolerance(POT_TOLERANCE);
//		elevationPID.setOutputRange(-0.7,  0.7);
		
	}
	
	public Claw(int winchMotorPort, int actuatorPort, int potPort){
		this(new VictorSP(winchMotorPort), new Solenoid(actuatorPort), new AnalogPotentiometer(potPort));
	}
	
	public void open() {
		actuator.set(false);
	}
	
	public void close() {
		actuator.set(true);
	}
	
	public boolean isOpen() {
		return !actuator.get();
	}
	
	/**
	 * If in manual mode this method will manually control the winch and return true.
	 * If in automatic mode this method will do nothing, and return false.
	 * @param speed
	 */
	public void liftManually(double speed) {
		setManual();
		double adjustedSpeed = ThresholdHandler.handleThreshold(speed, 0.1);
		//System.out.println(speed + " | " + adjustedSpeed);
		if (adjustedSpeed > 1){
			adjustedSpeed = 1;
		} else if (adjustedSpeed < -1){
			adjustedSpeed = -1;
		}
		winchMotor.set(adjustedSpeed);
	}
	
	public void setPID(double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		
		elevationPID.setPID(kP, kI, kD);
	}
	
	public void setSetpoint(double d) {
		setAutomatic();
		elevationPID.setSetpoint(d);
	}
	
	public void updateWinchPeriodic() {
		if (isAutomatic()) {
			double dummyInput = dummyWinch.get();
			double deltaHeight = Robot.clapper.getChangeInHeightInInches();

			winchMotor.set(dummyInput + 0.2 * deltaHeight); //TODO: take data and then derive formula
			System.out.println(" " + dummyInput + ", " + deltaHeight);
		}
	}

	public double getP() {
		return kP;
	}
	
	public double getI() {
		return kI;
	}
	
	public double getD() {
		return kD;
	}
	
	public double getPotValue() {
		return potScaled.pidGet();
	}
	
	public double getPercentHeight() {
		return (potScaled.pidGet() - LOWEST_POS)/POS_RANGE;
	}
	
	public double getInchHeight() {
		// TODO: Test
		return(potScaled.pidGet() - LOWEST_POS)/INCH_RANGE + 11.25;
	}
	
	/**
	 * Sets the claw to automatic control, PID will control the winch, moveManually will not function
	 */
	public void setAutomatic() {
		automatic = true;
		elevationPID.enable();
	}
	
	/**
	 * Sets the claw to manual control, PID will not control elevation, but the moveManually method will function. 
	 */
	public void setManual() {
		automatic = false;
		elevationPID.disable();
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
	
	public boolean isPidOnTarget() {
		return elevationPID.onTarget(); 
	}
	
	public double getError() {
		return elevationPID.getError();
	}

	public double translateClapperSetpoint(double clapperSetpoint) {
		
		int toteCount = Robot.toteCounter.getCount();
		
		if(clapperSetpoint == Clapper.ABOVE_RATCHET_SETPOINT) {
			if(toteCount == 1)
				return ONE_TOTE_LOADING;
			if(toteCount == 2)
				return TWO_TOTE_LOADING;
			if(toteCount == 3)
				return THREE_TOTE_LOADING;
			if(toteCount == 4)
				return FOUR_TOTE_LOADING;
			if(toteCount == 5)
				return FIVE_TOTE_LOADING;
		}
		
		if(clapperSetpoint == Clapper.ON_RATCHET_SETPOINT) {
			if(toteCount == 1)
				return ONE_TOTE_RESTING;
			if(toteCount == 2)
				return TWO_TOTE_RESTING;
			if(toteCount == 3)
				return THREE_TOTE_RESTING;
			if(toteCount == 4)
				return FOUR_TOTE_RESTING;
			if(toteCount == 5)
				return FIVE_TOTE_RESTING;
		}
		
		throw new IllegalArgumentException("Clapper setpoint " + clapperSetpoint + " invalid.");
	}
}

