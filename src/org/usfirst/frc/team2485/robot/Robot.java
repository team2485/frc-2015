
package org.usfirst.frc.team2485.robot;

import org.usfirst.frc.team2485.subsystems.DriveTrain;
import org.usfirst.frc.team2485.util.Controllers;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends IterativeRobot {
	
	private VictorSP left, left2, right, right2, center;
	public static DriveTrain drive;
	private Solenoid suspension;
//	private Compressor compressor;
	private DoubleSolenoid ds;
	
    public void robotInit() {

    	left 	= new VictorSP(7); //left: 7,8
    	left2 	= new VictorSP(8);
    	right   = new VictorSP(0); //right: 0, 1
    	right2 	= new VictorSP(1);
    	center  = new VictorSP(9); //center: 9
    	suspension = new Solenoid(7); //may need two solenoids
    	drive = new DriveTrain(left, left2, right, right2, center, suspension);
//    	ds = new DoubleSolenoid(7, 7);

//    	compressor = new Compressor();

    	Controllers.set(new Joystick(0), new Joystick(1));
    	System.out.println("initialized");
    }

    public void autonomousInit() {
    }
  
    public void autonomousPeriodic() {
    }
    
    public void teleopInit() {
    	System.out.println("teleop init");
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
        
//        else {
//        	drive.setSolenoid(false);
//        }
//        
//    	System.out.println("Pot value: " + pot.get());
//    	System.out.println("Enc value: " + encoder.get());
//    	System.out.println("IMU pitch: " + imu.getPitch());
//    	System.out.println("IMU yaw: " + imu.getYaw());
//    	System.out.println("IMU roll: " + imu.getRoll());
    	
    }
    
    public void disabledPeriodic(){

    }
    
    public void testPeriodic() {
//    	compressor.start();
    }
    
}
