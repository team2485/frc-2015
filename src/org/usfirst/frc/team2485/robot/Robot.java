
package org.usfirst.frc.team2485.robot;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.subsystems.DriveTrain;
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
	
	private VictorSP left, left2, right, right2, center;
	public static DriveTrain drive;
	private Solenoid suspension;
	private Compressor compressor;
	private DoubleSolenoid ds;
	private IMUAdvanced imu;
	private SerialPort ser;
	
    public void robotInit() {

    	left 	= new VictorSP(14); //left: 7,8
    	left2 	= new VictorSP(15);
    	right   = new VictorSP(0); //right: 0, 1
    	right2 	= new VictorSP(1);
    	center  = new VictorSP(13); //center: 9   changed to 13 1/31/15
    	suspension = new Solenoid(7); //may need two solenoids
    	
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
    	
    	drive = new DriveTrain(left, left2, right, right2, center, suspension, imu);
    	drive.setSolenoid(false);

    	Controllers.set(new Joystick(0), new Joystick(1));
    	System.out.println("initialized");
    }

    public void autonomousInit() {
    }
  
    public void autonomousPeriodic() {
    }
    
    public void teleopInit() {
    	System.out.println("teleop init");
    	
    	drive.setSolenoid(false);
    }

    public void teleopPeriodic() {
    	
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
        	drive.tuneKP(.005);
        if (Controllers.getButton(Controllers.XBOX_BTN_BACK))
        	drive.tuneKP(-.005);
        if (Controllers.getButton(Controllers.XBOX_BTN_Y)) 
        	drive.resetButtonClicked(); 
        
//    	System.out.println("current kP is: " + drive.kP_G_Rotate);

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
    
    public void disabledPeriodic(){

    }
    
    public void testPeriodic() {
    	compressor.start();
    }
    
}
