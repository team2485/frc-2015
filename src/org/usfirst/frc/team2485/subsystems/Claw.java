package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.util.CombinedSpeedController;
import org.usfirst.frc.team2485.util.DummyOutput;
import org.usfirst.frc.team2485.util.InvertedScaledPot;
import org.usfirst.frc.team2485.util.ThresholdHandler;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * @author Aidan Fay
 * @author Ben Clark
 * @author Anoushka Bose
 * @author Patrick Wamsley
 */

public class Claw {
	
	//To keep the claw and the clapper in sync, a low kP value lets the clapper push the claw when it needs to.
	//kP Lock Position in Place is only used when going from manual mode to automatic mode and keep the
	//claw in place.
	
	public static final double kP_STANDARD = 0.0075, kI = 0.00, kD = 0; 
	public static final double kP_LOCK_POSITION_IN_PLACE = 0.01;
//	public static final double kP_TEMP_AGGRESSIVE = 0.005;
	private SpeedController winchMotor;
	private Solenoid actuator;
//	private ScaledPot potScaled; 
	private InvertedScaledPot potScaled;
	private DummyOutput dummyWinch;
	 
	public static final double 	LOWEST_POS	= 210, 
								HIGHEST_POS	= 925; 
	
	private static final double POT_RANGE     	= HIGHEST_POS - LOWEST_POS;
	public  static final double POT_TOLERANCE 	= 12;
	private static final double INCH_RANGE    	= 60; // 15.625 in from floor up to 74.5 in
	private static final double INCH_LOW_POS 	= 15.625;
	
	@SuppressWarnings("unused")
	public static final double POTS_PER_INCH = POT_RANGE/INCH_RANGE;
	
	public static final double
						MANUAL_SAFETY_ABOVE_RACHET_POS 	= LOWEST_POS + 210, // AB - 4/2
						CONTAINER_ADJUSTMANT_POS		= LOWEST_POS + 465, // AB - 4/2
						ONE_AND_TWO_TOTE_RESTING_POS	= LOWEST_POS + 520, // AB - 4/2
						TWO_TOTE_PLACEMENT_POS			= LOWEST_POS + 286, // AB - 4/2 
						DROP_SEQ_POS_1					= LOWEST_POS + (POTS_PER_INCH * .5), 
						DROP_SEQ_POS_2					= LOWEST_POS + 140, // AB - 4/2
						DROP_SEQ_POS_3					= LOWEST_POS + 245, // AB - 4/2
						DROP_SEQ_POS_4					= LOWEST_POS + 360, // AB - 4/2
						DROP_SEQ_POS_5					= LOWEST_POS + 486, //CHECK
						DROP_SEQ_POS_6					= HIGHEST_POS - (POTS_PER_INCH * .5); 
	 
	public PIDController elevationPID;
	
	private boolean automatic = true;

	public Claw(SpeedController clawMotor, Solenoid clawSolenoid, AnalogPotentiometer pot) {
		this.winchMotor = new CombinedSpeedController(clawMotor);
		this.actuator 	= clawSolenoid;
		this.potScaled	= new InvertedScaledPot(pot); 
		this.dummyWinch = new DummyOutput();
		
		elevationPID = new PIDController(kP_STANDARD, kI, kD, potScaled, this.dummyWinch);
		elevationPID.setAbsoluteTolerance(POT_TOLERANCE);
		elevationPID.setOutputRange(-0.5,  0.5);
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
	 * If in manual mode this method will manually control the winch
	 * @param speed
	 */
	public void liftManually(double speed) {
		
		setManual();
		
		double adjustedSpeed = ThresholdHandler.deadbandAndScale(speed, ThresholdHandler.STANDARD_THRESHOLD, .1 , 1); 
	
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
		elevationPID.setPID(kP, kI, kD);
	}
	
	public void setSetpoint(double d) {
		setAutomatic();
		elevationPID.setSetpoint(d);
	}
	
	public void updateWinchPeriodic() {
		if (isAutomatic()) 
			winchMotor.set(dummyWinch.get());  
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
		return (potScaled.pidGet() - LOWEST_POS)/POT_RANGE;
	}
	
	public double getInchHeight() {
		return(potScaled.pidGet() - LOWEST_POS)/POT_RANGE * INCH_RANGE + 15.625; 
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
	
	public void setWinch(double input) {        
		winchMotor.set(input); 
	}
	
	public boolean isClawAboutToCollideWithRachet(double speedInput){
		return ((getPotValue() < MANUAL_SAFETY_ABOVE_RACHET_POS) && 
				(speedInput < 0) && Robot.ratchet.isExtended());
	}
}

