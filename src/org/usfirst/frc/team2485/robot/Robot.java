
package org.usfirst.frc.team2485.robot;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.subsystems.DriveTrain;
import org.usfirst.frc.team2485.subsystems.DualEncoder;
import org.usfirst.frc.team2485.subsystems.Strongback;
import org.usfirst.frc.team2485.util.Controllers;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends IterativeRobot {
	
	private VictorSP left, left2, right, right2, center, leadScrewMotor;
	public static DriveTrain drive;
//	public static Strongback strongback; 
	private Encoder leftEnc, rightEnc, centerEnc;
	private DualEncoder dualEncoder;
	private Solenoid suspension;
	private Compressor compressor;
	private DoubleSolenoid ds;
	private IMUAdvanced imu;
	private SerialPort ser;
	
    public void robotInit() {

    	left 	= new VictorSP(14); //left: 14,15
    	left2 	= new VictorSP(15);
    	right   = new VictorSP(0); //right: 0, 1
    	right2 	= new VictorSP(1);
    	center  = new VictorSP(13); //center: 9   changed to 13 1/31/15
    	suspension = new Solenoid(7); //may need two solenoids
    	
    	leftEnc = new Encoder(0, 1);
    	rightEnc = new Encoder(4, 5);
    	dualEncoder = new DualEncoder(leftEnc, rightEnc);
    	
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
    	
    	drive = new DriveTrain(left, left2, right, right2, center, suspension, imu, leftEnc, rightEnc, centerEnc);
    	drive.setSolenoid(false);
    	
//    	strongback = new Strongback(leadScrewMotor, imu); 

    	Controllers.set(new Joystick(0), new Joystick(1));
    	System.out.println("initialized");
    }

    public void autonomousInit() {
    	imu.zeroYaw();
    	leftEnc.reset();
    	rightEnc.reset();
    	dualEncoder.reset();
    }
  
    public void autonomousPeriodic() {
    	System.out.println("left/right " + leftEnc.getDistance() + "\t\t" + rightEnc.getDistance());
    	System.out.println("dualEnc " + dualEncoder.getDistance());
    	
    	drive.setLeftRight(-.7, -.7);
    }
    
    public void teleopInit() {
    	System.out.println("teleop init");
//    	imu.zeroYaw();

    	
//    	drive.setSolenoid(false);
    	
    	leftEnc.reset();
    	rightEnc.reset();
    }

    public void teleopPeriodic() {
    	
//		strongback.enablePid();
    	
        drive.warlordDrive(Controllers.getAxis(Controllers.XBOX_AXIS_LX, 0.2f),
        					Controllers.getAxis(Controllers.XBOX_AXIS_LY, 0.2f),
                			Controllers.getAxis(Controllers.XBOX_AXIS_RX, 0.2f));
    	
        if(Controllers.getButton(Controllers.XBOX_BTN_RBUMP)) {
        		drive.setQuickTurn(true);
        } else
        	drive.setQuickTurn(false);
//        
//        if(Controllers.getButton(Controllers.XBOX_BTN_LBUMP)) {
//        	
//        	drive.setLowSpeed();
//        }
//        
        if(Controllers.getButton(Controllers.XBOX_BTN_A)) {
        	drive.setSolenoid(true);
//        	ds.set(DoubleSolenoid.Value.kForward);
        }
        
        if(Controllers.getButton(Controllers.XBOX_BTN_B)) {
        	drive.setSolenoid(false);
//        	ds.set(DoubleSolenoid.Value.kReverse);
        }
        
        if (Controllers.getButton(Controllers.XBOX_BTN_START))
        	drive.tuneStrafeParam(.005);
        if (Controllers.getButton(Controllers.XBOX_BTN_BACK))
        	drive.tuneStrafeParam(-.005);
        if (Controllers.getButton(Controllers.XBOX_BTN_Y)) 
        	drive.resetButtonClicked(); 
        
    	System.out.println("current setpoint is: " + drive.imuPID.getSetpoint());
    	System.out.println("current error is: " + drive.imuPID.getError());
    

//        else {
//        	drive.setSolenoid(false);
//        }
//        
//    	System.out.println("Pot value: " + pot.get());
//    	System.out.println("Enc value: " + encoder.get());
//    	System.out.println("IMU pitch: " + imu.getPitch());
//    	System.out.println("IMU yaw: " + imu.getYaw());
//    	System.out.println("IMU roll: " + imu.getRoll());
    	
    	//basic controls for intake arm
        /*clapper.handleTote(Controllers.getAxis(Controllers.JOYSTICK_AXIS_Y,
        			Controllers.getAxis(Controllers.JOYSTICK_AXIS_Z);
        	
      	if (Controllers.getJoystickButton(1)) {
       		clapper.openClapper();
       	}
       	if (Controllers.getJoystickButton(2)) {
        	clapper.closeClapper();
    	}*/
    }
    
    public void disabledPeriodic() {
    	
    }
    
    public void testInit() {
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
