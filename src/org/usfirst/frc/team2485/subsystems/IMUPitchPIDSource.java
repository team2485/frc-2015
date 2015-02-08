package org.usfirst.frc.team2485.subsystems;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;

import edu.wpi.first.wpilibj.PIDSource;

public class IMUPitchPIDSource implements PIDSource {
	
	private IMU imu; 
	
	public IMUPitchPIDSource(IMU imu) {
		this.imu = imu; 
	}

	@Override
	public double pidGet() {
		return -imu.getPitch(); 
	}

}
