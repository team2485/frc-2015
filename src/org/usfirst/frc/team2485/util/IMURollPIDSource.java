package org.usfirst.frc.team2485.util;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;

import edu.wpi.first.wpilibj.PIDSource;

public class IMURollPIDSource implements PIDSource {
	
	private IMU imu; 
	
	public IMURollPIDSource(IMU imu) {
		this.imu = imu; 
	}

	@Override
	public double pidGet() {
		return imu.getRoll(); 
	}

}
