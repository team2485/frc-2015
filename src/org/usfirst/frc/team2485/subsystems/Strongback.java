package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.util.IMUPitchPIDSource;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * 
 * @author Patrick Wamsley
 */
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
		
	public Strongback(VictorSP leadScrew, IMU imu) {
		this.leadScrew = leadScrew; 
		this.imu = imu; 
				
		pitchPIDSource = new IMUPitchPIDSource(imu); 
		
		leadScrewImuPID = new PIDController(leadScrew_kP, leadScrew_kI, leadscrew_kD, pitchPIDSource, leadScrew); 
		leadScrewImuPID.setAbsoluteTolerance(absToleranceLeadScrew );
		leadScrewImuPID.setSetpoint(pitchSetpoint);
	}
	
	public Strongback(int leadScrewPort, IMU imu) {
		this(new VictorSP(leadScrewPort), imu);
	}
	
	public void disablePid() {
		leadScrewImuPID.disable();
	}
	
	public void enablePid() {
		leadScrewImuPID.enable();
	}
	
	public void setSetpoint(double newSetpoint) {
		pitchSetpoint = newSetpoint;
		leadScrewImuPID.setSetpoint(pitchSetpoint);
		leadScrewImuPID.enable();
	}
	
	public double getError() {
		return leadScrewImuPID.getError(); 
	}
}
