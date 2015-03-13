
package org.usfirst.frc.team2485.robot;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.auto.Sequencer;
import org.usfirst.frc.team2485.auto.SequencerFactory;
import org.usfirst.frc.team2485.subsystems.Clapper;
import org.usfirst.frc.team2485.subsystems.Claw;
import org.usfirst.frc.team2485.subsystems.ContainerCommandeerer;
import org.usfirst.frc.team2485.subsystems.DriveTrain;
import org.usfirst.frc.team2485.subsystems.Fingers;
import org.usfirst.frc.team2485.subsystems.RatchetSystem;
import org.usfirst.frc.team2485.subsystems.Strongback;
import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.Controllers;
import org.usfirst.frc.team2485.util.DualEncoder;
import org.usfirst.frc.team2485.util.ToteCounter;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Anoushka Bose
 * @author Aidan Fay
 * @author Ben Clark
 * @author Patrick Wamsley
 * @author Camille Considine
 * @author Mike Maunu
 */ 
public class Robot extends IterativeRobot {
	
	//Subsystems 
	public static DriveTrain drive;
	public static Strongback strongback; 
	public static Clapper clapper;
	public static Fingers fingers;
	public static RatchetSystem ratchet;
	public static Claw claw;
	public static ContainerCommandeerer containerCommandeerer;
	public static ToteCounter toteCounter;
	private Compressor compressor; 
//	private CameraServer camServer; 
	
	//Speed Controllers
	private CombinedVictorSP leftDrive, rightDrive, centerDrive;
	private CombinedVictorSP clapperLifter;
	private VictorSP strongbackMotor;  
	private VictorSP leftFingerBelt, rightFingerBelt;
	private VictorSP clawMotor;
	
	//Solenoids
	private Solenoid centerWheelSuspension;
	private Solenoid ratchetLatchActuator;
	private Solenoid shortFingerActuators, longFingerActuators; 
	private Solenoid commandeererSolenoidRight, commandeererSolenoidLeft;
	private DoubleSolenoid clapperActuator, clawSolenoid;
	
	//Sensors
	private Encoder leftEnc, rightEnc, centerEnc; 
	private DualEncoder dualEncoderVelCalc;
	
	private IMUAdvanced imu;

	private DigitalInput clapperSafetyLimitSwitch; 
	private DigitalInput toteDetectorLimitSwitch; 
	private DigitalInput pressureSwitch;

	private AnalogPotentiometer clapperPot;
	private AnalogPotentiometer clawPot;
	
	//Tote Count
	private long timeLastToteCountProcessed;
	private long TOTE_COUNT_MIN_DELAY = 500;
	
	//Sequences	&& Auto
	private Sequencer autoSequence, currTeleopSequence;
	private boolean finishedRotating = false;
	private int degrees;
	
	//Data
	private double curPos, lastPos;
	private static double currVelocity;
	private double lastVelocity; 
	
	//Other needed variables 
	private Relay compressorSpike;
	
    public void robotInit() {
    	drive						= new DriveTrain(new VictorSP(11), new Talon(2), new VictorSP(3), new Talon(4), 
    									new VictorSP(5), new Talon(6));
//    	leftDrive     				= new CombinedVictorSP(new VictorSP(14), new VictorSP(15)); // talon and victorsp
//    	rightDrive       			= new CombinedVictorSP(new VictorSP(0), new VictorSP(1)); // talon and victorsp
//    	centerDrive		 			= new CombinedVictorSP(new VictorSP(11), new VictorSP(7)); // talon and victorsp
//    	clapperLifter 				= new CombinedVictorSP(new VictorSP(13), new VictorSP(3)); 
//    	strongbackMotor 			= new VictorSP(2); 
//    	leftFingerBelt   	 		= new VictorSP(9); 
//    	rightFingerBelt  	 		= new VictorSP(6); 
//    	clawMotor					= new VictorSP(12);   	
    	
//    	centerWheelSuspension		= new Solenoid(3);
//    	ratchetLatchActuator 		= new Solenoid(2);
//    	shortFingerActuators 		= new Solenoid(6);
//    	longFingerActuators  		= new Solenoid(5);
//    	commandeererSolenoidLeft	= new Solenoid(1, 2);
//    	commandeererSolenoidRight	= new Solenoid(1, 0);
//    	clapperActuator 			= new DoubleSolenoid(1,7);
//    	clawSolenoid				= new DoubleSolenoid(0,4); 
    	
//    	leftEnc = new Encoder(0, 1);
//    	rightEnc = new Encoder(4, 5);

//    	dualEncoderVelCalc = new DualEncoder(leftEnc, rightEnc);
    	
//    	leftEnc.setDistancePerPulse(.0414221608);
//    	rightEnc.setDistancePerPulse(.0414221608); 
//    	
//    	try {
//    		byte update_rate_hz = 50;
//    		imu = new IMUAdvanced(new SerialPort(57600, SerialPort.Port.kUSB), update_rate_hz);
//    	} catch (Exception ex) {
//    		System.out.println("imu failed to init");
//    		ex.printStackTrace();
//    	}
//    	
//    	if (imu != null) 
//    		LiveWindow.addSensor("IMU", "Gyro", imu);
//    	
//    	clapperSafetyLimitSwitch = new DigitalInput(16); 
//    	toteDetectorLimitSwitch  = new DigitalInput(17);
//    	pressureSwitch			 = new DigitalInput(10); //TODO: find port
//    	
//    	clawPot		    		= new AnalogPotentiometer(0);
//    	clapperPot		   		= new AnalogPotentiometer(1);  
//
////    	compressor = new Compressor();
//    	compressorSpike = new Relay(0);
//    	
//    	
////    	camServer = CameraServer.getInstance();
//        //camServer.setQuality(50);
//        //the camera name (ex "cam0") can be found through the roborio web interface
////        camServer.startAutomaticCapture("cam1");
//    	
    	toteCounter = new ToteCounter(); 
    	drive = new DriveTrain(leftDrive, rightDrive, centerDrive, centerWheelSuspension, imu, leftEnc, rightEnc, centerEnc);
//       	clapper = new Clapper(clapperLifter, clapperActuator, clapperPot, toteDetectorLimitSwitch, clapperSafetyLimitSwitch);
//    	claw    = new Claw(clawMotor, clawSolenoid, clawPot);
//    	fingers = new Fingers(leftFingerBelt, rightFingerBelt, longFingerActuators, shortFingerActuators);
//    	ratchet = new RatchetSystem(ratchetLatchActuator);    	
//    	strongback = new Strongback(strongbackMotor, imu); 
//    	containerCommandeerer = new ContainerCommandeerer(commandeererSolenoidLeft, commandeererSolenoidRight);
//    	
        Controllers.set(new Joystick(0), new Joystick(1), new Joystick(2));
    	
    	System.out.println("initialized");
    }

    public void autonomousInit() {
    	imu.zeroYaw();

    	leftEnc.reset();
    	rightEnc.reset();
//    	dualEncoder.reset();

    	resetAndDisableSystems();
    	
    	//        int autonomousType = (int) SmartDashboard.getNumber("autoMode", SequencerFactory.DRIVE_TO_AUTO_ZONE);
//        autoSequence = SequencerFactory.createAuto(autonomousType);
        autoSequence = SequencerFactory.createAuto(SequencerFactory.DRIVE_TO_AUTO_ZONE);
    	
    }
  
    public void autonomousPeriodic() {
  	 
    	 if (autoSequence != null) {
    		if (autoSequence.run()) {
    			autoSequence = null;
    		}
    	}
    	 
    }
    
    public void teleopInit() {
		resetAndDisableSystems();
    }	
    
    private void resetAndDisableSystems() {
    	
    	currTeleopSequence = null;
    	
    	drive.setMaintainHeading(false);
    	drive.dropCenterWheel(false);
    	drive.disableDriveStraightPID();
    	drive.disableIMUPID();
//    	drive.disableStrafePID(); 	//keep this commented out as long as there is no center encoder
    	drive.setMotors(0.0, 0.0, 0.0);
    	
    	clapper.setManual();
    	clapper.liftManually(0.0);
    	
    	fingers.stopBelts();
    	
    	claw.setManual();
    	claw.liftManually(0.0);
    	
    	strongback.setSetpoint(0.0);
    	strongback.disablePid();
    	
    	containerCommandeerer.resetSol();
    	
    }

    public void teleopPeriodic() {
    	
    	if (Controllers.getButton(Controllers.XBOX_BTN_A)) 
    		strongback.enablePid();
    	else if (Controllers.getButton(Controllers.XBOX_BTN_B))
    		strongback.disablePid();
    	
    	strongback.checkSafety();
    	
       	updateDashboard();
	
       	  ////////////////////////////////////////////
       	 ///////////	 DRIVE CODE	   //////////////
       	////////////////////////////////////////////
     
       	//controls changed 2/16/15 per driver request
       	if (Controllers.getAxis(Controllers.XBOX_AXIS_RTRIGGER, .2f) > 0) 
       		drive.setForcedNoStrafeMode(true);
       	else 
       		drive.setForcedNoStrafeMode(false);
       	
       	if (Controllers.getAxis(Controllers.XBOX_AXIS_LTRIGGER, .2f) > 0) 
       		drive.setSlowStrafeOnlyMode(true);
       	else 
       		drive.setSlowStrafeOnlyMode(false);
      
       	if (Controllers.getButton(Controllers.XBOX_BTN_RBUMP)) 
       		drive.setQuickTurn(true);
       	else
       		drive.setQuickTurn(false);		
       	
        if (Controllers.getButton(Controllers.XBOX_BTN_LBUMP))
        	drive.setNormalSpeed(); 	
        else 
        	drive.setLowSpeed();
        
        if (Controllers.getButton(Controllers.XBOX_BTN_BACK))
        	drive.dropCenterWheel(false); 
        if (Controllers.getButton(Controllers.XBOX_BTN_START))
        	drive.dropCenterWheel(true);
        
        drive.warlordDrive(Controllers.getAxis(Controllers.XBOX_AXIS_LX, 0),
				Controllers.getAxis(Controllers.XBOX_AXIS_LY, 0),
    			Controllers.getAxis(Controllers.XBOX_AXIS_RX, 0));

		/////////////////////////////////////////////
		//////////		TOTE COUNTER
		/////////////////////////////////////////////
        
        long currTime = System.currentTimeMillis();
        if (Controllers.getButton(Controllers.XBOX_BTN_Y) && currTime - timeLastToteCountProcessed > TOTE_COUNT_MIN_DELAY) {
        	toteCounter.addTote(); 
        	timeLastToteCountProcessed = currTime;
        } else if (Controllers.getButton(Controllers.XBOX_BTN_X) && currTime - timeLastToteCountProcessed > TOTE_COUNT_MIN_DELAY) {
//        	toteCounter.reset(); 
        	toteCounter.subtractTote(); //this is stupid
        	timeLastToteCountProcessed = currTime;
        } 
           
		/////////////////////////////////////////////
		//////////		PSEUDO-VELOCITY CALCULATIONS
		/////////////////////////////////////////////

       	double curPos = dualEncoderVelCalc.getDistance();
       	currVelocity = curPos-lastPos;
//       	System.out.println(imu.getWorldLinearAccelX() +"," + imu.getWorldLinearAccelY() + "," + imu.getWorldLinearAccelZ() + "," + imu.getPitch() + "," + imu.getRoll() + "," + imu.getYaw() + "," + curPos + "," + curVelocity + "," + (curVelocity - lastVelocity));
       
       	lastPos = curPos;
       	lastVelocity = currVelocity;
       	
       	
		/////////////////////////////////////////////
		///////////////CLAPPER LOGIC////////////////
		///////////////////////////////////////////
//       	if (clapperSafetyLimitSwitch.get()) {
////    		System.out.println("CLAPPERS TOO LOW");
//    		if (clapper.isAutomatic())
//    			clapper.setManual(); 
//    	}
       	if (Controllers.getOperatorJoystickOneAxis(Controllers.JOYSTICK_AXIS_Y,(float) 0.1) != 0) {//if the joystick is moved
    		clapper.liftManually((Controllers.getOperatorJoystickOneAxis(Controllers.JOYSTICK_AXIS_Y,(float) 0.1))); //back is up
    	}
    	else if (clapper.isManual()){
    		//System.out.println("enabling clapper PID after manual operation");
    		clapper.setSetpoint(clapper.getPotValue());//set the setpoint to where ever it left off
    	}
//    	else if (clapper.isBelowLowestSetPoint()) {
//    		clapper.clapperPID.disable();
//    	}
//       	
       	clapper.updateToteCount(toteCounter.getCount());
       	
       	if (Controllers.getOperatorJoystickOneButton(1) && currTeleopSequence == null) {
       		currTeleopSequence = SequencerFactory.createToteIntakeRoutine();
       	}
       	if (Controllers.getOperatorJoystickOneButton(2) && currTeleopSequence == null) {
    		currTeleopSequence = SequencerFactory.createToteIntakeNoHang();
    	}
       	
       	if(Controllers.getOperatorJoystickOneButton(3))
       		clapper.openClapper();
       	if(Controllers.getOperatorJoystickOneButton(4))
       		clapper.closeClapper();
       	

		/////////////////////////////////////////////
		//////////		FINGERS, RATCHET, AND ONE CLAPPER SETPOINT 
       	//////////		(ONLY ENABLING A SINGLE CLAPPER SETPOINT)
		/////////////////////////////////////////////

       	//FINGERS, RATCHET, AND ONE CLAPPER SETPOINT (ONLY ENABLING A CLAPPER SETPOINT)
       	if(Controllers.getOperatorJoystickOneAxis(Controllers.JOYSTICK_AXIS_Z) > .7){
       		fingers.rotateToteRight(.7);
       	} else if(Controllers.getOperatorJoystickOneAxis(Controllers.JOYSTICK_AXIS_Z) < -.7){
       		fingers.rotateToteLeft(.7);
       	} else if(Controllers.getOperatorJoystickOneButton(5)){
       		fingers.dualIntake(1);
    	} else if(Controllers.getOperatorJoystickOneButton(6)){
       		fingers.dualReverse(.75);
    	} else {
       		fingers.dualIntake(0); 
    	}
       	
       	if (Controllers.getOperatorJoystickOneButton(7)) {
       		//System.out.println("fingers should close now");
       		fingers.setFingerPosition(Fingers.CLOSED);
       	}
       	if (Controllers.getOperatorJoystickOneButton(8)) {
//       		clapper.setSetpoint(Clapper.COOP_THREE_TOTES_SETPOINT); 
       		clapper.setSetpoint(Clapper.COOP_THREE_TOTES_SETPOINT);
       	}
       	if (Controllers.getOperatorJoystickOneButton(9)) {
//       		System.out.println("fingers should go parallel");
//       		fingers.setFingerPosition(Fingers.PARALLEL);
       		fingers.setFingerPosition(Fingers.PARALLEL);
       	}
       	if (Controllers.getOperatorJoystickOneButton(10)) {
//       		System.out.println("hook should go back to normal");
       		ratchet.extendRatchet();
       	}
       	if (Controllers.getOperatorJoystickOneButton(11)) {
//       		System.out.println("fingers should open");
       		fingers.setFingerPosition(Fingers.OPEN);
       	}
       	if (Controllers.getOperatorJoystickOneButton(12)) {
//       		System.out.println("hook should release");
       		ratchet.retractRatchet();
       	}

       	
       	///////////////////////////////////////////////////////////////////////
       	///////////////////////////////////////////////////////////////////////
       	//
       	//	SECONDARY JOYSTICK CONTROLS...ALL CONTAINER/CLAW RELATED
       	//
       	///////////////////////////////////////////////////////////////////////
       	///////////////////////////////////////////////////////////////////////
       	
       	
       	if (Controllers.getOperatorJoystickTwoAxis(Controllers.JOYSTICK_AXIS_Y, .1f) != 0) {
       		claw.liftManually(Controllers.getOperatorJoystickTwoAxis(Controllers.JOYSTICK_AXIS_Y));
       	} else if (claw.isManual()) {
       		claw.setPID(Claw.kP_LOCK_POSITION_IN_PLACE, 0, 0);
    		claw.setSetpoint(claw.getPotValue());
       	}
       	
       	if(Controllers.getOperatorJoystickTwoButton(1) && currTeleopSequence == null)
       		currTeleopSequence = SequencerFactory.createContainerPickupRoutine();
       	if(Controllers.getOperatorJoystickTwoButton(2) && currTeleopSequence == null)
       		currTeleopSequence = SequencerFactory.createPrepareForContainerLiftRoutine();
       	
    	if(Controllers.getOperatorJoystickTwoButton(3))
       		claw.open();
       	if(Controllers.getOperatorJoystickTwoButton(4))
       		claw.close();
       	
       	if (Controllers.getOperatorJoystickTwoButton(5) && currTeleopSequence == null) {
       		currTeleopSequence = SequencerFactory.createContainerRightingRoutine();
       	}
       	
       	if (Controllers.getOperatorJoystickTwoButton(6)) {
//       		null so far
       	}
       	
       	if(Controllers.getOperatorJoystickTwoButton(7)) {
       		claw.setPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Claw.kI, Claw.kD);
       		claw.setSetpoint(Claw.PLACE_ON_EXISTING_STACK_SIX_TOTES);
       	}
       	
       	//////////////////////////////////////////////
		//////////////////////////////////////////////
		//////////////////////////////////////////////
       	if(Controllers.getOperatorJoystickTwoButton(8) && currTeleopSequence == null) {
//       		teleopSequence = SequencerFactory.createTestPickupWithStrongbackTilt();
       	}
       	//////////////////////////////////////////////
		//////////////////////////////////////////////
		//////////////////////////////////////////////
       	
       	if(Controllers.getOperatorJoystickTwoButton(9)) {
       		claw.setPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Claw.kI, Claw.kD);
       		claw.setSetpoint(Claw.PLACE_ON_EXISTING_STACK_FIVE_TOTES);
       	}
       	
       	if(Controllers.getOperatorJoystickTwoButton(10) && currTeleopSequence == null) {
       		currTeleopSequence = SequencerFactory.createDropToteStackRoutine(true);//totes on the ratchet and one underneath
       	}
       	
       	if(Controllers.getOperatorJoystickTwoButton(11)) {
       		claw.setPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Claw.kI, Claw.kD);
       		claw.setSetpoint(Claw.PLACE_ON_EXISTING_STACK_FOUR_TOTES);
//      		claw.setSetpoint(Claw.ONE_TOTE_RESTING);
       	}
       	
       	if(Controllers.getOperatorJoystickTwoButton(12) && currTeleopSequence == null) {
       		currTeleopSequence = SequencerFactory.createDropToteStackRoutine(false);//only totes on the ratchet
       	}
       		
   		if ((Controllers.getOperatorJoystickOneAxis(Controllers.JOYSTICK_AXIS_THROTTLE) > 0) ||
   				Controllers.getOperatorJoystickTwoAxis(Controllers.JOYSTICK_AXIS_THROTTLE) > 0) {
   			//kill ALL THE THINGS@!#@#!!@@
   			if(currTeleopSequence != null) {
   				currTeleopSequence.clear();
   				currTeleopSequence = null;
   			}
   			System.out.println("Killing all the things");
   		}
   	
       	
       	if (currTeleopSequence != null) {
//       		System.out.println("running sequence here in teleopPeriodic");
       		if (currTeleopSequence.run()) {
       			currTeleopSequence = null;
       		}
       	}
       	
       	claw.updateWinchPeriodic();
    	clapper.updateLastHeight(); 

    	
//    	System.out.println("Strongback: isEnabled " + strongback.leadScrewImuPID.isEnable() + "\t\tSetpoint" + strongback.leadScrewImuPID.getSetpoint());
    }
    
    public static double getCurrVelocity() {
		return currVelocity;
	}

    public void disabledInit() {
    	resetAndDisableSystems();
    }
    
	public void disabledPeriodic() {
//    	System.out.println(clapper.getPotValue());
//    	int counter = 0;
//    	
//    	if (Controllers.getButton(Controllers.XBOX_BTN_A)) {
//    		counter++;
//    	}
//    	
//    	if(counter > 50) {
//    		degrees += 30;
////    		System.out.println("degrees is now " + degrees);
//    		counter = 0;
//    	}
		if(currTeleopSequence != null) {
			System.out.println("teleopSequence not null in disabledPeriodic");
			currTeleopSequence.clear();
			currTeleopSequence = null;
		}
    	updateDashboard();
    }
    
    public void testInit() {
    	resetAndDisableSystems();
    	finishedRotating = false;
    }
        
    public void testPeriodic() {
    	
    	if (!pressureSwitch.get()) 
    		compressorSpike.set(Relay.Value.kForward);
    	else
    		compressorSpike.set(Relay.Value.kOff);
    	
//    	compressor.start();
  	
//    	clapper.setSetpoint(Clapper.ON_RATCHET_SETPOINT);
//    	claw.close(); 
//    	claw.setSetpoint(Claw.ONE_TOTE_LOADING);
//    	claw.setSetpoint(Claw.ONE_TOTE_RESTING);
    	
//    	drive.dropCenterWheel(false);
    	
//    	if(!finishedRotating && drive.rotateTo(30)) {
//    		finishedRotating = true;
//    		System.out.println("just finished rotateTo inside of testPeriodic");
//    	}
       
//    	System.out.println("Imu yaw: " + imu.getYaw());
//    	System.out.println("Imu pitch: " + imu.getPitch());

//    	strongback.enablePid(); 
    }
    
    public void updateDashboard() {
     	SmartDashboard.putString("Clapper and Container", clapper.getPercentHeight() + "," + (int)claw.getPotValue() + ","+ claw.getPercentHeight()+ "," + (int)clapper.getPotValue() + "," + -1 * strongback.getIMURoll());  
//    	SmartDashboard.putString("Clapper and Container", clapper.getPercentHeight() + ","+ claw.getPercentHeight() + "," + strongback.getIMURoll());  	
       	SmartDashboard.putNumber("IPS", (int) drive.getAbsoluteRate());
       	SmartDashboard.putNumber("Battery", DriverStation.getInstance().getBatteryVoltage());
        SmartDashboard.putBoolean("Disabled", DriverStation.getInstance().isDisabled());
        SmartDashboard.putNumber("Claw Pot", claw.getPotValue());
//        System.out.println(claw.getPotValue());
        SmartDashboard.putNumber("Clapper Pot", clapper.getPotValue());
        SmartDashboard.putNumber("Tote Count", toteCounter.getCount());
        SmartDashboard.putNumber("Error from Claw", claw.getError());
        SmartDashboard.putNumber("Claw kP", claw.getP());
        //SmartDashboard.putBoolean("Clapper is manual: ", clapper.isManual());
        SmartDashboard.putNumber("Clapper Inches", clapper.getInchHeight());
        SmartDashboard.putNumber("Claw Inches", claw.getInchHeight());
        SmartDashboard.putNumber("Clapper change in height" ,  (float)Robot.clapper.getChangeInHeightInInches());
        SmartDashboard.putNumber("Encoder Distance", leftEnc.getDistance());
        SmartDashboard.putBoolean("Tote detected by limit switch", clapper.toteDetected());
    }
}
