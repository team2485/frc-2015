
package org.usfirst.frc.team2485.robot;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.auto.Sequencer;
import org.usfirst.frc.team2485.auto.SequencerFactory;
import org.usfirst.frc.team2485.subsystems.Clapper;
import org.usfirst.frc.team2485.subsystems.Claw;
import org.usfirst.frc.team2485.subsystems.DriveTrain;
import org.usfirst.frc.team2485.subsystems.Fingers;
import org.usfirst.frc.team2485.subsystems.RatchetSystem;
import org.usfirst.frc.team2485.subsystems.Strongback;
import org.usfirst.frc.team2485.util.Controllers;
import org.usfirst.frc.team2485.util.DualEncoder;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
//	public static Strongback strongback;
	public static Clapper clapper;
	public static Fingers fingers;
<<<<<<< HEAD
	public static RatchetSystem rachet;
=======
>>>>>>> 226fed499fbeec706f7eb17032e6c8ba3355f73b
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
	
//	boolean fingersOn = true;
	
    public void robotInit() {

    	left     	= new VictorSP(14); //left: 14,15
    	left2 	    = new VictorSP(15);
    	right       = new VictorSP(0); //right: 0, 1
    	right2  	= new VictorSP(1);
    	leftBelt    = new VictorSP(9);
    	rightBelt   = new VictorSP(7);
    	clapperLifter1 = new VictorSP(13);
    	clapperLifter2 = new VictorSP( 3);
    	longFingerActuators  = new Solenoid(0);
    	shortFingerActuators = new Solenoid(4);
    	latchActuator = new Solenoid(3);
    	
//    	center  = new VictorSP(13); //center: 9   changed to 13 1/31/15
//    	suspension = new Solenoid(7); //may need two solenoids
    	clapperActuator = new DoubleSolenoid(6, 5);
    	clapperPot = new AnalogPotentiometer(1); 
    	
    	leftEnc = new Encoder(0, 1);
    	rightEnc = new Encoder(4, 5);
    	dualEncoder = new DualEncoder(leftEnc, rightEnc);
    	
    	toteDetector = new AnalogInput(0);
    	
//    	leadScrewMotor = new VictorSP(2); 
    	
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
    	
//    	drive = new DriveTrain(left, left2, right, right2, imu, leftEnc, rightEnc);
//    	drive.setSolenoid(false);
    	
    	//clapper = new Clapper(6, 5);
    	clapper = new Clapper(clapperLifter1, clapperLifter2, clapperActuator, clapperPot);
    	fingers = new Fingers(leftBelt,rightBelt,longFingerActuators,shortFingerActuators);
    	rachet = new RatchetSystem(latchActuator);
//    	clapper.close();
    	
//    	strongback = new Strongback(leadScrewMotor, imu); 
    	
    	
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
    	
    	autoSequence = SequencerFactory.createAuto(SequencerFactory.SEQ_TEST);
    }
  
    public void autonomousPeriodic() {
//    	System.out.println("left/right " + leftEnc.getDistance() + "\t\t" + rightEnc.getDistance());
//    	System.out.println("dualEnc " + dualEncoder.getDistance());
    	
//    	drive.setLeftRight(-.7, -.7);
    	 autoSequence.run();
    	 
    }
    
    public void teleopInit() {
    	System.out.println("teleop init");
//    	imu.zeroYaw();

    	
//    	drive.setSolenoid(false);
    	
    	leftEnc.reset();
    	rightEnc.reset();
    }

    public void teleopPeriodic() {
    	
    	System.out.println("Teleop enabled");
    	
    	clapper.setManual();
    	
    	clapper.moveManually(Controllers.getJoystickAxis(Controllers.JOYSTICK_AXIS_X,0));//left is up
    	
//		strongback.enablePid();
    	
//        drive.warlordDrive(Controllers.getAxis(Controllers.XBOX_AXIS_LX, 0.2f),
//        					Controllers.getAxis(Controllers.XBOX_AXIS_LY, 0.2f),
//                			Controllers.getAxis(Controllers.XBOX_AXIS_RX, 0.2f));
    	
//        if(Controllers.getButton(Controllers.XBOX_BTN_RBUMP)) {
//        		drive.setQuickTurn(true);
//        } else
//        	drive.setQuickTurn(false);
//        
//        if(Controllers.getButton(Controllers.XBOX_BTN_A)) {
//        	drive.setSolenoid(true);
//        }
//        
//        if(Controllers.getButton(Controllers.XBOX_BTN_B)) {
//        	drive.setSolenoid(false);
//        }
        
//        if (Controllers.getButton(Controllers.XBOX_BTN_START))
//        	drive.tuneStrafeParam(.005);
//        if (Controllers.getButton(Controllers.XBOX_BTN_BACK))
//        	drive.tuneStrafeParam(-.005);
//        if (Controllers.getButton(Controllers.XBOX_BTN_Y)) 
//        	drive.resetButtonClicked(); 
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
//        fingers.handleTote((Controllers.getAxis(Controllers.JOYSTICK_AXIS_Y)),
//        			Controllers.getAxis(Controllers.JOYSTICK_AXIS_Z));
//    
      	if (Controllers.getAxis(Controllers.JOYSTICK_AXIS_THROTTLE) > 0) {
	      	clapper.openClapper();
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
       	
       	
       	
    }
    
    public void disabledPeriodic() {
    	
    }
    
    public void testInit() {
//    	leftEnc.reset();
//    	rightEnc.reset();
//    	drive.disableDriveStraightPID();
//    	drive.disableIMUPID();
//    	imu.zeroYaw();
//    	done = false;
    }
    
    private boolean done = false;
    public void testPeriodic() {
    	compressor.start();

//    	if(!done && drive.rotateTo(-173))
//    		done = true;
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

    }
    
}
