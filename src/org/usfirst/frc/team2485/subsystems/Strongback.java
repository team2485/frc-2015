package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.team2485.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;

public class Strongback {

	//with serial port facing the front of the robot, imu reports a neg pitch when the strongback
	//tilts back
	
	//serial port is the "tail" of the imu
	private VictorSP leadScrew; 
	private IMU imu; 
	public PIDController leadScrewImuPID;
	private double pitchSetpoint = 0.0;
	private double absToleranceLeadScrew = 0.15; //degrees 
	
	private IMUPitchPIDSource pitchPIDSource; 
	
	public static final double
		leadScrew_kP = 0.15,
		leadScrew_kI = 0.0, 
		leadscrew_kD = 0.0; 
	
//	private double leadScrewPWM; 
	
	public Strongback(VictorSP leadScrew, IMU imu) {
		this.leadScrew = leadScrew; 
		this.imu = imu; 
		
		System.out.println("imu in strongback class is null = " + (imu == null));
		
		pitchPIDSource = new IMUPitchPIDSource(imu); 
		
		leadScrewImuPID = new PIDController(leadScrew_kP, leadScrew_kI, leadscrew_kD, pitchPIDSource, leadScrew); 
		leadScrewImuPID.setAbsoluteTolerance(absToleranceLeadScrew );
		leadScrewImuPID.setSetpoint(pitchSetpoint);
//		leadScrewImuPID.enable(); 
	}
	
	public void disablePid() {
		leadScrewImuPID.disable();
	}
	
	public void enablePid() {
		leadScrewImuPID.enable();
		System.out.println("enablePid method called");
	}
	
	public void setSetpoint(double newSetpoint) {
		pitchSetpoint = newSetpoint;
		leadScrewImuPID.setSetpoint(pitchSetpoint);
	}
	
	public double getError() {
		return leadScrewImuPID.getError(); 
	}
}
