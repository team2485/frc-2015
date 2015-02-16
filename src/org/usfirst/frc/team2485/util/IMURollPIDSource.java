package org.usfirst.frc.team2485.util;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.robot.Robot;

import edu.wpi.first.wpilibj.PIDSource;

public class IMURollPIDSource implements PIDSource {
	
	private IMUAdvanced imu;
	private double encoderRate;
	private boolean isDataFiltered; 
	
	private static final double MAGIC_ROLL_CORRECTION_SCALAR = 3; 
	
	public boolean isDataFiltered() {
		return isDataFiltered;
	}

	public void filterData(boolean movementCorrected) {
		this.isDataFiltered = movementCorrected;
	}

	public IMURollPIDSource(IMUAdvanced imu, boolean filterData) {
		this.imu = imu; 
		this.isDataFiltered = filterData;
	}

	@Override
	public double pidGet() {
		encoderRate = Robot.getCurVelocity();
		if (isDataFiltered) {
			if (Math.abs(encoderRate) > .02)
				System.out.println(imu.getRoll() +  " \t:\t " + encoderRate * MAGIC_ROLL_CORRECTION_SCALAR);
			return (imu.getRoll() - encoderRate * MAGIC_ROLL_CORRECTION_SCALAR);// returns a value adjusted by equation found by Todor
		} else {
			return imu.getRoll(); 
		}
	}

}
