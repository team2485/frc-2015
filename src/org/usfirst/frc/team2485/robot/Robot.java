
package org.usfirst.frc.team2485.robot;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.auto.Sequencer;
import org.usfirst.frc.team2485.auto.SequencerFactory;
import org.usfirst.frc.team2485.subsystems.*;
import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.Controllers;
import org.usfirst.frc.team2485.util.DualEncoder;
import org.usfirst.frc.team2485.util.ToteCount;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Anoushka Bose
 * @author Aidan Fay
 * @author Ben Clark
 * @author Patrick Wamsley
 * @author Camille Considine
 * @author Mike Maunu
 */ 
public class Robot extends IterativeRobot {
	
	//subsystems 
	public static DriveTrain drive;
	public static Strongback strongback; 
	public static Clapper clapper;
	public static Fingers fingers;
	public static RatchetSystem ratchet;
	public static Claw claw;
	public static ToteCount toteCounter;

	
	private VictorSP left, left2, right, right2, strongbackMotor, leftFingerBelt, rightFingerBelt, clapperLifter1, clapperLifter2;
	private CombinedVictorSP center; 
	 
	private Encoder leftEnc, rightEnc, centerEnc;
	private DualEncoder dualEncoder;
	
	private Solenoid centerWheelSuspension, longFingerActuators, shortFingerActuators, latchActuator;
	private Compressor compressor;
	private DoubleSolenoid clapperActuator;
	private IMUAdvanced imu;
	private SerialPort ser;
	private CameraServer camServer; 
	
	private Sequencer autoSequence;
	private AnalogPotentiometer clapperPot;
	private CombinedVictorSP combinedVictorSP;
	private Sequencer teleopSequence;
	
	int degrees;
	private double curPos;
	private double lastPos;
	private double lastVelocity;
	private static double curVelocity;
	
	private boolean toteCounterButtonIsReset = true; 
    private boolean done = false;
	private Solenoid clawSolenoid;
	private VictorSP clawMotor;
	private AnalogPotentiometer clawPot;
	
//	boolean fingersOn = true;
	
    public void robotInit() {
    	
    	left     				= new VictorSP(14); //left: 14,15
    	left2 	    			= new VictorSP(15);
    	right       			= new VictorSP(0); //right: 0, 1
    	right2  				= new VictorSP(1);
    	leftFingerBelt    		= new VictorSP(9); // could be 6
    	rightFingerBelt   		= new VictorSP(6); //could be 9
    	clawMotor				= new VictorSP(12);
    	clapperLifter1 			= new VictorSP(13); 
    	clapperLifter2 			= new VictorSP(3); 
    	strongbackMotor 			= new VictorSP(2); 
    	center		 			= new CombinedVictorSP(new VictorSP(11), new VictorSP(7)); 
    	
    	longFingerActuators  	= new Solenoid(5);
    	shortFingerActuators 	= new Solenoid(6);
    	latchActuator 			= new Solenoid(2);
    	centerWheelSuspension	= new Solenoid(3); 
    	clawSolenoid			= new Solenoid(4);
    	clapperActuator 		= new DoubleSolenoid(7, 1);
    	
    	clawPot		    		= new AnalogPotentiometer(0);
    	clapperPot		   		= new AnalogPotentiometer(1);  
    	
    	leftEnc = new Encoder(0, 1);
    	rightEnc = new Encoder(4, 5);
    	dualEncoder = new DualEncoder(leftEnc, rightEnc);
    	
    	leftEnc .setDistancePerPulse(.0414221608);
    	rightEnc.setDistancePerPulse(.0414221608); 
    	
    	toteCounter = new ToteCount(); 

    	compressor = new Compressor();
    	try{
    		ser = new SerialPort(57600, SerialPort.Port.kUSB);
    		byte update_rate_hz = 50;
    		imu = new IMUAdvanced(ser, update_rate_hz);
    	
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	if(imu != null) {
    		LiveWindow.addSensor("IMU", "Gyro", imu);
    	}
    	
    	drive = new DriveTrain(left, left2, right, right2, center, centerWheelSuspension, imu, leftEnc, rightEnc, centerEnc);
       	clapper = new Clapper(clapperLifter1, clapperLifter2, clapperActuator, clapperPot);
    	claw    = new Claw(clawMotor, clawSolenoid, clawPot);
    	fingers = new Fingers(leftFingerBelt,rightFingerBelt,longFingerActuators,shortFingerActuators);
    	ratchet = new RatchetSystem(latchActuator);    	
    	strongback = new Strongback(strongbackMotor, imu); 
    	
    	
//    	camServer = CameraServer.getInstance();
//        //camServer.setQuality(50);
//        //the camera name (ex "cam0") can be found through the roborio web interface
//        camServer.startAutomaticCapture("cam1");
    	
        Controllers.set(new Joystick(0), new Joystick(1));
    	
        
        
    	System.out.println("initialized");
    }

    public void autonomousInit() {
//    	imu.zeroYaw();
//    	leftEnc.reset();
//    	rightEnc.reset();
//    	dualEncoder.reset();
//    	strongback.disablePid(); 
//    	
//    	autoSequence = SequencerFactory.createAuto(SequencerFactory.THREE_TOTE_STRAIGHT);
//    	autoSequence.reset();
//    	autoSequence = null;
    	
    }
  
    public void autonomousPeriodic() {
////    	System.out.println("left/right " + leftEnc.getDistance() + "\t\t" + rightEnc.getDistance());
////    	System.out.println("dualEnc " + dualEncoder.getDistance());
//    	
////    	drive.setLeftRight(-.7, -.7);
////    	 autoSequence.run();
//    	 
//    	 if (autoSequence != null) {
////    		System.out.println("running teleop sequence");
//    		if (autoSequence.run()) {
//    			autoSequence = null;
////    			clapper.setManual(); 
//    		}
//    	}
//    	 
    }
    
    public void teleopInit() {
    	System.out.println("teleop init");
//    	imu.zeroYaw();
    	
    	
    	drive.setMaintainHeading(false);
    	drive.dropCenterWheel(false);
    	
    	drive.disableDriveStraightPID();
    	drive.disableIMUPID();
//    	drive.disableStrafePID(); 	//keep this commented out as long as there is no center encoder
    	
    	leftEnc.reset();
    	rightEnc.reset();
    	
    	clapper.setManual();
    	claw.setManual();
    	
//		strongback.enablePid();
		
		teleopSequence = null; 
//    	strongback.setSetpoint(0);
//		System.out.println(clapper.getPotValue());
    }

    public void teleopPeriodic() {
    	
    	compressor.start();

//    	strongback.checkSafety();
    	
       	updateDashboard();
    	
    	 if (Controllers.getAxis(Controllers.XBOX_AXIS_RTRIGGER, .2f) > 0) {
         	drive.setHighSpeed();
         }
         else if (Controllers.getAxis(Controllers.XBOX_AXIS_LTRIGGER, .2f) > 0) {
         	drive.setLowSpeed();
         }
         else {
         	drive.setNormalSpeed(); 
         }
    	
        if(Controllers.getButton(Controllers.XBOX_BTN_RBUMP)) {
        		drive.setQuickTurn(true);
        } else
        	drive.setQuickTurn(false);
        
        if(Controllers.getButton(Controllers.XBOX_BTN_A)) {
        	drive.dropCenterWheel(true);
        }
        
        if(Controllers.getButton(Controllers.XBOX_BTN_B)) {
        	drive.dropCenterWheel(false);
        }
        
        if (Controllers.getButton(Controllers.XBOX_BTN_LBUMP)) {
        	drive.setSlowStrafeOnlyMode(true);
        }
        else {
        	drive.setSlowStrafeOnlyMode(false);
        }
        
        drive.warlordDrive(Controllers.getAxis(Controllers.XBOX_AXIS_LX, 0),
				Controllers.getAxis(Controllers.XBOX_AXIS_LY, 0),
    			Controllers.getAxis(Controllers.XBOX_AXIS_RX, 0));
             
//    	System.out.println("current setpoint is: " + drive.imuPID.getSetpoint());
//    	System.out.println("current error is: " + drive.imuPID.getError());
//        
//    	System.out.println("Pot value: " + pot.get());
//    	System.out.println("Enc value: " + encoder.get());
//    	System.out.println("IMU pitch: " + imu.getPitch());
//    	System.out.println("IMU yaw: " + imu.getYaw());
//    	System.out.println("IMU roll: " + imu.getRoll());
        
        if (Controllers.getButton(Controllers.XBOX_BTN_A) && toteCounterButtonIsReset) {
        	toteCounter.addTote(); 
        	toteCounterButtonIsReset = false; 
        } else if (Controllers.getButton(Controllers.XBOX_BTN_B) && toteCounterButtonIsReset) {
//        	toteCounter.reset(); 
        	toteCounter.addTote(-1); //this is stupid
        	toteCounterButtonIsReset = false; 
        } else {
        	toteCounterButtonIsReset  = true; 
        }

       	double curPos = dualEncoder.getDistance();
       	curVelocity = curPos-lastPos;
//       	System.out.println(imu.getWorldLinearAccelX() +"," + imu.getWorldLinearAccelY() + "," + imu.getWorldLinearAccelZ() + "," + imu.getPitch() + "," + imu.getRoll() + "," + imu.getYaw() + "," + curPos + "," + curVelocity + "," + (curVelocity - lastVelocity));
       	

       	
       	lastPos = curPos;
       	lastVelocity = curVelocity;
       	
       	if (Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_X,(float) 0.1) != 0) {//if the joystick is moved
    		clapper.liftManually((Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_X,(float) 0.1)));//right is up
    	}
    	else if (clapper.isManual()){
    		clapper.setSetpoint(clapper.getPotValue());//set the setpoint to where ever it left off
    	}
    	else if (clapper.isBelowLowestSetPoint()) {
    		clapper.clapperPID.disable();
    	}
       	
       	
       	if (Controllers.getJoystickButton(1)) {
       		teleopSequence = SequencerFactory.toteSuckIn();
       		//claw.close();
       	}
       	if (Controllers.getJoystickButton(2)) {
    		teleopSequence = null;
    		return;
    	}
       	if (Controllers.getJoystickButton(3)) {
       		System.out.println("Joystick button 3 pressed, claw should move down");
       		claw.liftManually(0.4);
       	} else if (Controllers.getJoystickButton(4)) {
       		System.out.println("joystick button 4 pressed, claw should move up");
       		claw.liftManually(-0.4);
       	} else { //if (claw.isManual()){
    		//claw.setSetpoint(claw.getPotValue());
       		claw.liftManually(0);
       	}
       	
       	if (Controllers.getJoystickButton(5)) {
       		
       		claw.close();
       	}
       	
       	if (Controllers.getJoystickButton(6)) {
       		claw.open();
       	}
       	
       	if (Controllers.getJoystickButton(7)) {
       		System.out.println("fingers should close now");
       		fingers.setFingerPosition(Fingers.CLOSED);
       	}
       	if (Controllers.getJoystickButton(8)) {
       		teleopSequence = SequencerFactory.createIntakeToteRoutineBackup();       		
       	}
       	if (Controllers.getJoystickButton(9)) {
       		System.out.println("fingers should go parallel");
       		fingers.setFingerPosition(Fingers.PARALLEL);
       	}
       	if (Controllers.getJoystickButton(10)) {
       		System.out.println("hook should go back to normal");
       		ratchet.setDefaultRatchetPosition();
       	}
       	if (Controllers.getJoystickButton(11)) {
       		System.out.println("fingers should open");
       		fingers.setFingerPosition(Fingers.OPEN);
       	}
       	if (Controllers.getJoystickButton(12)) {
       		System.out.println("hook should release");
       		ratchet.releaseToteStack();
       	}
       	
       	if (teleopSequence != null) {
       		if (teleopSequence.run()) {
       			teleopSequence = null;
       		}
       	}
       	
       	if (!(clapper.isManual()))
    		fingers.handleTote((Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_Y)),
    				Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_Z));

       	if(teleopSequence == null) {
       		if ((Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_THROTTLE) > 0)) {
       			clapper.openClapper();
       			//TODO: figure out how to only open the clapper if a sequence isn't running
       		} else {
       			clapper.closeClapper();
       		}
       	}
    }
    
    public static double getCurVelocity() {
		return curVelocity;
	}

	public void disabledPeriodic() {
//    	System.out.println(clapper.getPotValue());
    	int counter = 0;
    	
    	if (Controllers.getButton(Controllers.XBOX_BTN_A)) {
    		counter++;
    	}
    	
    	if(counter > 50) {
    		degrees += 30;
//    		System.out.println("degrees is now " + degrees);
    		counter = 0;
    	}
    	
    }
    
    public void testInit() {
    	clapper.setManual();
    	leftEnc.reset();
    	rightEnc.reset();
    	drive.disableDriveStraightPID();
    	drive.disableIMUPID();
    	imu.zeroYaw();
    	done = false;
    }
        
    public void testPeriodic() {
    	
//    	leftFingerBelt.set(.2);
    	
    	clawMotor.set(.2);

//    	compressor.start();

//    	drive.setLeftRight(.2, -.2);
//    	drive.driveTo(60);
    	
//    	degrees = 30;
    	
//    	drive.dropCenterWheel(false);
//    	if(!done && drive.rotateTo(30)) {
//    		done = true;
//    		System.out.println("just finished rotateTo inside of testPeriodic");
//    	}
    	
//    	System.out.println(imu.getYaw());
    	
//    	
//    	  if (Controllers.getButton(Controllers.XBOX_BTN_START))
//          	drive.tuneDriveKp(.005);
//          if (Controllers.getButton(Controllers.XBOX_BTN_BACK))
//          	drive.tuneDriveKp(-.005);
//          if (Controllers.getButton(Controllers.XBOX_BTN_Y)) 
//          	drive.resetButtonClicked(); 
          
          
//    	System.out.println("Imu yaw: " + imu.getYaw());
//    	System.out.println("Imu pitch: " + imu.getPitch());
//    	
//    	left.set(-.5);
//    	left2.set(-.5); 
//    	right.set(.5);
//    	right2.set(.5);
    	
//    	leadScrewMotor.set(-.05);
//    	System.out.println(strongback.leadScrewImuPID.isEnable());

//    	strongback.enablePid(); 
//		System.out.println(strongback.getError() + " output " + .leadScrewImuPID.get());
    	
//    	SmartDashboard.putString("Clapper and Container", clapper.getPercentHeight() +"," + 0 + "," + imu.getRoll());
       	
//       	SmartDashboard.putInt("IPS",    (int) drive.getAbsoluteRate());
       	

    }
    
    public void updateDashboard(){
     	SmartDashboard.putString("Clapper and Container", clapper.getPercentHeight() +"," + 0 + "," + strongback.getIMURoll());  	
       	SmartDashboard.putNumber("IPS", (int) drive.getAbsoluteRate());;
       	SmartDashboard.putNumber("battery", DriverStation.getInstance().getBatteryVoltage());
        SmartDashboard.putBoolean("disabled", DriverStation.getInstance().isDisabled());
        SmartDashboard.putNumber("claw pot", claw.getPotValue());
        SmartDashboard.putNumber("clapper pot", clapper.getPotValue());
        SmartDashboard.putNumber("tote count", toteCounter.get());
    }
    
}
