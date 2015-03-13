package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.auto.SequencedItems.SetClawPID;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.DummyOutput;
import org.usfirst.frc.team2485.util.ScaledPot;
import org.usfirst.frc.team2485.util.ThresholdHandler;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Aidan Fay
 * @author Ben Clark
 */

public class Claw {
	
	//To keep the claw and the clapper in sync, a low kP value lets the clapper push the claw when it needs to.
	//kP Lock Position in Place is only used when going from manual mode to automatic mode and keep the
	//claw in place.
	
	private CombinedVictorSP winchMotor;
	private DoubleSolenoid actuator;
	private ScaledPot potScaled;
	private DummyOutput dummyWinch;
		
	private PIDController elevationPID;
	private boolean automatic = true;
	
	//for auto chasing the clapper  
	public static final double kP_CHASE_CLAPPER = 0.05;
	public static final int CHASE_TOLERANCE = 2;
	
	public static final double kP_LESS_POWER_ALLOWS_MORE_ERROR = 0.0025, kI = 0.00, kD = 0; // TODO: check kP BADLY
	public static final double kP_LOCK_POSITION_IN_PLACE = 0.035;

	private static final double LOWEST_POS    = 73; 	
	private static final double PICKUP_POS	  = LOWEST_POS + 45;// top: 850 bottom: 112
	private static final double POS_RANGE     = 740;
	public  static final double POT_TOLERANCE = 12;
	private static final double INCH_RANGE    = 63.75; // 11.25 in from floor (corresponds to a pot value of LOWEST_POS) - 75 in
	
	private static final double LOADING_RESTING_OFFSET = 50, FIRST_LOADING_RESTING_OFFSET = 200; 
	
	private static final double TOTE_HEIGHT = 140;
	
	public static final double 
		CONTAINER_LOADING_POINT	= LOWEST_POS + 2,
		HIGHEST_POS				= LOWEST_POS + POS_RANGE - 5,
		ONE_TOTE_RESTING		= LOWEST_POS + 286, // TODO: tune //366
		ONE_TOTE_LOADING		= ONE_TOTE_RESTING + FIRST_LOADING_RESTING_OFFSET, // 469
		TWO_TOTE_RESTING		= LOWEST_POS + 319, // TODO: make independent of one tote resting, should depend on lowest pos
		TWO_TOTE_LOADING		= TWO_TOTE_RESTING + LOADING_RESTING_OFFSET,
		THREE_TOTE_RESTING		= TWO_TOTE_RESTING + TOTE_HEIGHT,
		THREE_TOTE_LOADING		= THREE_TOTE_RESTING + LOADING_RESTING_OFFSET,
		FOUR_TOTE_RESTING		= THREE_TOTE_RESTING + TOTE_HEIGHT,
		FOUR_TOTE_LOADING		= FOUR_TOTE_RESTING + LOADING_RESTING_OFFSET,
		FIVE_TOTE_RESTING		= FOUR_TOTE_RESTING + TOTE_HEIGHT,
		FIVE_TOTE_LOADING		= FIVE_TOTE_RESTING + LOADING_RESTING_OFFSET,
		RATCHET_COLLISION		= LOWEST_POS + 160,
		FIX_CONTAINER_POSITION_IN_CLAW	= LOWEST_POS + 185,
		FIRST_TOTE_POSITION_BELOW_RATCHET	= LOWEST_POS + 65;
	
	public static final double 	PLACE_ON_EXISTING_STACK_THREE_TOTES = LOWEST_POS + 455,
								PLACE_ON_EXISTING_STACK_FOUR_TOTES = LOWEST_POS + 600,
								
								PLACE_ON_EXISTING_STACK_FIVE_TOTES = LOWEST_POS + 750,
								PLACE_ON_EXISTING_STACK_SIX_TOTES = LOWEST_POS + POS_RANGE;
	
	public static final double NO_CONTAINER_OFFSET = -120;
	 
	
	// top of the claw: 870
	// bottom of the claw: 118

	public Claw(VictorSP winchMotor, DoubleSolenoid actuator, AnalogPotentiometer pot) {
		this.winchMotor = new CombinedVictorSP(winchMotor);
		this.winchMotor.invertMotorDirection(true);
		this.actuator 	= actuator;
		this.potScaled	= new ScaledPot(pot);
		this.dummyWinch = new DummyOutput();
		
		elevationPID = new PIDController(kP_LESS_POWER_ALLOWS_MORE_ERROR, kI, kD, potScaled, this.dummyWinch);
		elevationPID.setAbsoluteTolerance(POT_TOLERANCE);
		elevationPID.setOutputRange(-0.5,  0.5);
		
	}
	
	public Claw(int winchMotorPort, int actuatorPort1, int actuatorPort2, int potPort){
		this(new VictorSP(winchMotorPort), new DoubleSolenoid(actuatorPort1, actuatorPort2), new AnalogPotentiometer(potPort));
	}
	
	public void open() {
		actuator.set(DoubleSolenoid.Value.kForward);
	}
	
	public void close() {
		actuator.set(DoubleSolenoid.Value.kReverse);
	}
	
	public boolean isOpen() {
		return actuator.get().equals(DoubleSolenoid.Value.kForward);
	} 
	
	/**
	 * If in manual mode this method will manually control the winch
	 * If in automatic mode this method will do nothing.
	 * @param speed
	 */
	public void liftManually(double speed) {
		setManual();
		double adjustedSpeed = ThresholdHandler.deadbandAndScale(speed, ThresholdHandler.STANDARD_THRESHOLD, 0 , 1); //TODO: instead of 0 and 1, find wanted values
		//System.out.println(speed + " | " + adjustedSpeed);
		if (adjustedSpeed > 1)
			adjustedSpeed = 1;
		else if (adjustedSpeed < -1)
			adjustedSpeed = -1;

		if (isClawAboutToCollideWithRachet(adjustedSpeed))
			winchMotor.set(0);
		 else 
			winchMotor.set(adjustedSpeed);
		
	}
	
	public void setPID(double kP, double kI, double kD) {
//		this.kP = kP;
//		this.kI = kI;
//		this.kD = kD;
		
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
	//		System.out.println(" " + dummyInput + ", " + deltaHeight);
		}
	}

	public double getP() {
		return elevationPID.getP();
	}
	
	public double getI() {
		return elevationPID.getI();
	}
	
	public double getD() {
		return elevationPID.getD();
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
		
		this.setPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD());
		
		int toteCount = Robot.toteCounter.getCount();
		
		if (clapperSetpoint == Clapper.ABOVE_RATCHET_SETPOINT || 
				clapperSetpoint == Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT) {
			if (toteCount == 1)
				return ONE_TOTE_LOADING;
			if (toteCount == 2)
				return TWO_TOTE_LOADING;
			if (toteCount == 3)
				return THREE_TOTE_LOADING;
			if (toteCount == 4)
				return FOUR_TOTE_LOADING;
			if (toteCount == 5)
				return FIVE_TOTE_LOADING;
		}
		
		if (clapperSetpoint == Clapper.ON_RATCHET_SETPOINT || clapperSetpoint == Clapper.HOLDING_TOTE_SETPOINT || 
				clapperSetpoint == Clapper.LOADING_SETPOINT) {
			if (toteCount == 1)
				return ONE_TOTE_RESTING;
			if (toteCount == 2)
				return TWO_TOTE_RESTING;
			if (toteCount == 3)
				return THREE_TOTE_RESTING;
			if (toteCount == 4)
				return FOUR_TOTE_RESTING;
			if (toteCount == 5)
				return FIVE_TOTE_RESTING;
		}
		throw new IllegalArgumentException("Clapper setpoint " + clapperSetpoint + " invalid.");
	}
	
	public boolean isClawAboutToCollideWithRachet(double speedInput){
		return ((getPotValue() < RATCHET_COLLISION + 150) && (getPotValue() > RATCHET_COLLISION) && 
				(speedInput < 0) && Robot.ratchet.isExtended());
	}
	
	public void setWinch(double input) {
		winchMotor.set(input); 
	}
}

