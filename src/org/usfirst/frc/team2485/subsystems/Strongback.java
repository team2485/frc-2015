package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.util.IMURollPIDSource;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * @author Patrick Wamsley
 */
public class Strongback {

	//with serial port facing the front of the robot, imu reports a neg pitch when the strongback
	//tilts back
	
	//serial port is the "tail" of the imu
	private SpeedController leadScrew; 
	private IMUAdvanced imu; 
	public PIDController leadScrewImuPID;
	private double rollSetpoint = 1.0; // THIS IS CHANGED
	private double absToleranceLeadScrew = 0.25; //degrees 
	
	private IMURollPIDSource rollPIDSource; 

	public static final double
		leadScrew_kP = 0.4,
		leadScrew_kI = 0.0, 
		leadscrew_kD = 0.0; 
		
	public Strongback(SpeedController strongbackMotor, IMUAdvanced imu) { 
		this.leadScrew = strongbackMotor; 
		this.imu = imu; 
				
		rollPIDSource = new IMURollPIDSource(this.imu,true); 
		
		leadScrewImuPID = new PIDController(leadScrew_kP, leadScrew_kI, leadscrew_kD, rollPIDSource, this.leadScrew); 
		leadScrewImuPID.setAbsoluteTolerance(absToleranceLeadScrew );
		leadScrewImuPID.setSetpoint(rollSetpoint);
	}
	
	public Strongback(int leadScrewPort, IMUAdvanced imu) {
		this(new VictorSP(leadScrewPort), imu);
	}
	
	public void disablePid() {
		leadScrewImuPID.disable();
	}
	
	public void enablePid() {
		leadScrewImuPID.enable();
	}
	
	/**
	 * Sets PID setpoint but does not enable the PID. 
	 */
	public void setSetpoint(double newSetpoint) {
		rollSetpoint = newSetpoint;
		leadScrewImuPID.setSetpoint(rollSetpoint);
	}
	
	public double getError() {
		return leadScrewImuPID.getError(); 
	}
	
	public double getIMURoll(){
		return rollPIDSource.pidGet();
	}
	
	public void checkSafety() {
		if (Math.abs(leadScrewImuPID.getError()) > 12) {
			System.out.println("ERROR in check safety");
			leadScrewImuPID.disable();
		}
	}
}
