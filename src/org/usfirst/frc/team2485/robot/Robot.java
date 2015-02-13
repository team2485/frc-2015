
package org.usfirst.frc.team2485.robot;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.auto.Sequencer;
import org.usfirst.frc.team2485.auto.SequencerFactory;
import org.usfirst.frc.team2485.subsystems.*;
import org.usfirst.frc.team2485.util.CombinedVictorSP;
import org.usfirst.frc.team2485.util.Controllers;
import org.usfirst.frc.team2485.util.DualEncoder;
import org.usfirst.frc.team2485.util.ThresholdHandler;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Anoushka
 * @author Aidan
 * @author Ben
 * @author Patrick
 * @author Camille
 * @author Maunu
 */ 
public class Robot extends IterativeRobot {
	
	private VictorSP left, left2, right, right2, center, leadScrewMotor, leftBelt, rightBelt, clapperLifter1, clapperLifter2;
	public static DriveTrain drive;
	public static Strongback strongback; 
	public static Clapper clapper;
	public static Fingers fingers;
	public static RatchetSystem ratchet;
	public static Claw claw; 
	private Encoder leftEnc, rightEnc, centerEnc;
	private DualEncoder dualEncoder;
	private Solenoid suspension, longFingerActuators, shortFingerActuators, latchActuator;
	private Compressor compressor;
	private DoubleSolenoid ds, clapperActuator;
	private IMUAdvanced imu;
	private SerialPort ser;
	private CameraServer camServer;
	
	private AnalogInput toteDetector;
	
	private Sequencer autoSequence;
	private AnalogPotentiometer clapperPot;
	private CombinedVictorSP combinedVic;
	private Sequencer teleopSequence;
	
//	boolean fingersOn = true;
	
    public void robotInit() {

    	left     	= new VictorSP(14); //left: 14,15
    	left2 	    = new VictorSP(15);
    	right       = new VictorSP(0); //right: 0, 1
    	right2  	= new VictorSP(1);
    	leftBelt    = new VictorSP(9);
    	rightBelt   = new VictorSP(7);
    	clapperLifter1 = new VictorSP(13);
    	clapperLifter2 = new VictorSP(3 );
    	longFingerActuators  = new Solenoid(0);
    	shortFingerActuators = new Solenoid(4);
    	latchActuator = new Solenoid(3);
    	
//    	center  = new VictorSP(13); //center: 9   changed to 13 1/31/15
    	suspension = new Solenoid(7); //may need two solenoids
    	clapperActuator = new DoubleSolenoid(6, 5);
    	clapperPot = new AnalogPotentiometer(1); 
    	
    	leftEnc = new Encoder(0, 1);
    	rightEnc = new Encoder(4, 5);
    	dualEncoder = new DualEncoder(leftEnc, rightEnc);
    	
    	toteDetector = new AnalogInput(0);
    	
    	leadScrewMotor = new VictorSP(2); 
    	
    	leftEnc.setDistancePerPulse(.0414221608);
    	rightEnc.setDistancePerPulse(.0414221608); 
    	
//    	ds = new DoubleSolenoid(7, 7);

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
    	
    	drive = new DriveTrain(left, left2, right, right2, center, latchActuator, imu, leftEnc, rightEnc, centerEnc);
//    	drive.setSolenoid(false);
    	
    	//clapper = new Clapper(6, 5);
    	clapper = new Clapper(clapperLifter1, clapperLifter2, clapperActuator, clapperPot);
    	fingers = new Fingers(leftBelt,rightBelt,longFingerActuators,shortFingerActuators);
    	ratchet = new RatchetSystem(latchActuator);
//    	clapper.close();
    	
    	strongback = new Strongback(leadScrewMotor, imu); 
    	
    	
//    	camServer = CameraServer.getInstance();
//        //camServer.setQuality(50);
//        //the camera name (ex "cam0") can be found through the roborio web interface
//        camServer.startAutomaticCapture("cam1");
    	
        Controllers.set(new Joystick(0), new Joystick(1));
    	
    	System.out.println("initialized");
    }

    public void autonomousInit() {
    	imu.zeroYaw();
    	leftEnc.reset();
    	rightEnc.reset();
    	dualEncoder.reset();
    	
    	autoSequence = SequencerFactory.createAuto(SequencerFactory.ONE_TOTE);
    }
  
    public void autonomousPeriodic() {
//    	System.out.println("left/right " + leftEnc.getDistance() + "\t\t" + rightEnc.getDistance());
//    	System.out.println("dualEnc " + dualEncoder.getDistance());
    	
//    	drive.setLeftRight(-.7, -.7);
    	 autoSequence.run();
    	 
    }
    
    public void teleopInit() {
    	System.out.println("teleop init");
    	imu.zeroYaw();

    	
//    	drive.setSolenoid(false);
    	
    	leftEnc.reset();
    	rightEnc.reset();
    	
    	clapper.setManual();
    	
		strongback.enablePid();
		
		teleopSequence = null; 
    }

    public void teleopPeriodic() {
    	strongback.checkSafety();
    	
//    	System.out.println("teleop enabled" );

    	
    	double adjustedJoystickXAxis = (ThresholdHandler.handleThreshold(Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_X,0), 0.1));
    	if (adjustedJoystickXAxis != 0){//if the joystick is moved
    		clapper.liftManually(adjustedJoystickXAxis);//left is up
    	}
    	else if (clapper.isManual()){
    		clapper.setSetpoint(clapper.getPotValue());//set the setpoint to where ever it left off
    		System.out.println("setting clapper setpoint in isManual detection, teleopPeriodic, getPotValue() is " + clapper.getPotValue());
    	}
    	
//		System.out.println(imu.getRoll());
    	
        drive.warlordDrive(Controllers.getAxis(Controllers.XBOX_AXIS_LX, 0.2f),
        					Controllers.getAxis(Controllers.XBOX_AXIS_LY, 0.2f),
                			Controllers.getAxis(Controllers.XBOX_AXIS_RX, 0.2f));
    	
        if(Controllers.getButton(Controllers.XBOX_BTN_RBUMP)) {
        		drive.setQuickTurn(true);
        } else
        	drive.setQuickTurn(false);
//        
//        if(Controllers.getButton(Controllers.XBOX_BTN_A)) {
//        	drive.setSolenoid(true);
//        }
//        
//        if(Controllers.getButton(Controllers.XBOX_BTN_B)) {
//        	drive.setSolenoid(false);
//        }
        
//        
//    	System.out.println("current setpoint is: " + drive.imuPID.getSetpoint());
//    	System.out.println("current error is: " + drive.imuPID.getError());
//        
//    	System.out.println("Pot value: " + pot.get());
//    	System.out.println("Enc value: " + encoder.get());
//    	System.out.println("IMU pitch: " + imu.getPitch());
//    	System.out.println("IMU yaw: " + imu.getYaw());
//    	System.out.println("IMU roll: " + imu.getRoll());

    	//basic controls for intake arm
        fingers.handleTote((Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_Y)),
        			Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_Z));
    
      	if (Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_THROTTLE) > 0) {
      		System.out.println("clapper should be open");
	      	clapper.openClapper();
	      	
	      	//TODO: figure out how to only open the clapper if a sequence isn't running
       	}
      	else {
        	clapper.closeClapper();
    	}
       	if (Controllers.getJoystickButton(7)) {
       		fingers.setFingerPosition(Fingers.CLOSED);
       	}
       	if (Controllers.getJoystickButton(9)) {
       		fingers.setFingerPosition(Fingers.PARALLEL);
       	}
       	if (Controllers.getJoystickButton(11)) {
       		fingers.setFingerPosition(Fingers.OPEN);
       	}
       	if (Controllers.getJoystickButton(3)) {
       		clapper.setSetpoint(clapper.COOP_TWO_TOTES_SETPOINT);
       	}
       	if (Controllers.getJoystickButton(4)) {
       		clapper.setSetpoint(clapper.COOP_ONE_TOTE_SETPOINT);
       	}
       	if (Controllers.getJoystickButton(5)) {
       		teleopSequence = SequencerFactory.createIntakeToteRoutine();
       	}
       	
       	if(teleopSequence != null) {
       		System.out.println("running teleop sequence");
       		if(teleopSequence.run()) {
       			teleopSequence = null;
       		}
       	}
       	
       	SmartDashboard.putString("Clapper and Container", clapper.getPercentHeight() +"," + 0 + "," + imu.getRoll());
       	
       	SmartDashboard.putInt("RPM", (int) drive.getAbsoluteRate());
       	
//       	System.out.println("setpoint " + clapper.getSetpoint() + " potValue " + clapper.getPotValue() + " pid controlled " + clapper.isAutomatic());
       	
    }
    
    public void disabledPeriodic() {
    	System.out.println(clapper.getPotValue());
    	
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
    
    private boolean done = false;
    public void testPeriodic() {

    	compressor.start();

//    	drive.setLeftRight(.2, -.2);
//    	drive.driveTo(60);
    	
//    	if(!done && drive.driveTo(60)) {
//    		done = true;
//    		System.out.println("just finished driveTo inside of testPeriodic");
//    	}
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
//		strongback.enablePid(); 
//		System.out.println(strongback.getError() + " output " + strongback.leadScrewImuPID.get());
    	
    	SmartDashboard.putString("Clapper and Container", clapper.getPercentHeight() +"," + 0 + "," + imu.getRoll());
       	
       	SmartDashboard.putInt("RPM", (int) drive.getAbsoluteRate());

    }
    
}
