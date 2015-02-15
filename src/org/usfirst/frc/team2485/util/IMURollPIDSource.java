package org.usfirst.frc.team2485.util;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.robot.Robot;

import edu.wpi.first.wpilibj.PIDSource;

public class IMURollPIDSource implements PIDSource {
	
	private IMUAdvanced imu;
	private double encoderRate;
	private boolean movementCorrected; 
	
	public boolean isMovementCorrected() {
		return movementCorrected;
	}

	public void setMovementCorrected(boolean movementCorrected) {
		this.movementCorrected = movementCorrected;
	}

	public IMURollPIDSource(IMUAdvanced imu, boolean movementCorrected) {
		this.imu = imu; 
		this.movementCorrected = movementCorrected;
	}

	@Override
	public double pidGet() {
		encoderRate = Robot.getCurVelocity();
		if (movementCorrected){
			System.out.println(imu.getRoll() + "-" + encoderRate*4);
			return (imu.getRoll() - encoderRate*4);// returns a value adjusted by equation found by Todor
		} else {
			return imu.getRoll(); 
		}
	}

}
