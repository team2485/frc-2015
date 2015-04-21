package org.usfirst.frc.team2485.util;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.robot.Robot;

import edu.wpi.first.wpilibj.PIDSource;

/**
 * 
 * A Roll PID source used with an IMU. Needed because the IMU's pidGet() method returns the yaw. <p>
 * 
 * WARNING: When the robot is moving quickly, the IMU reports Roll values that aren't accurate. This class attempts to acount for this
 * while data is being filtered. <p> 
 * 
 * Toggle data filtering with <code> filterData(boolean movementCorrected)</code>. 
 * 
 * @author Patrick Wamsley
 * @author Aidan Fay
 */

public class IMURollPIDSource implements PIDSource {
	
	private IMUAdvanced imu;
	private double encoderRate;
	private boolean isDataFiltered; 
	
	private static final double ROLL_CORRECTION_SCALAR = 3; 
		
	public IMURollPIDSource(IMUAdvanced imu, boolean filterData) {
		this.imu = imu; 
		this.isDataFiltered = filterData;
	}
	
	public boolean isDataFiltered() {
		return isDataFiltered;
	}

	public void filterData(boolean movementCorrected) {
		this.isDataFiltered = movementCorrected;
	}

	@Override
	public double pidGet() {
		
		encoderRate = Robot.getCurrVelocity();
		
		if (isDataFiltered) 
			return (double)Math.pow(1.0 - Math.min(Math.abs(encoderRate), 1.0), 
					ROLL_CORRECTION_SCALAR) * (imu.getRoll() - encoderRate * 4.0); 
		 else 
			return imu.getRoll(); 
		
	}

}

/* Another, untested, attempt to fix the Roll error from the IMU. 
package org.usfirst.frc.team2485.util;

import org.usfirst.frc.com.kauailabs.nav6.frc.IMU;
import org.usfirst.frc.com.kauailabs.nav6.frc.IMUAdvanced;
import org.usfirst.frc.team2485.robot.Robot;

import edu.wpi.first.wpilibj.PIDSource;

public class IMURollPIDSource implements PIDSource {
	
	private IMUAdvanced imu;
	private double encoderRate; 		// Current velocity. 
	private int currentState;			// Keep track of our state for the pidGet() method.
	private double timeInState = 0;		// Amount of time in current state.
	
	private static final int rs_RACING = 0;
	private static final int rs_SLOWING= 1;
	private static final int rs_NORMAL = 2;	
	
	private int myState = rs_NORMAL;
	private double lastRoll; 			// Keep track of last Roll, to see if changing.
	
	private boolean isDataFiltered;
	private int timeinState; 
	
	private static final double ROLL_CORRECTION_SCALAR = 4.0; 
	// Velocity at which we know we are running fast and should transition into or out of rs_RACING
	private static final double RACING_VELOCITY_THRESHOLD = 0.5; 
	// Interval between calls, so we can track how long we are in a particular state. This really should be derived from update_rate_hz in robotInit(). 
	private static final double INTERVAL_TIME = 20.0;
	// Maximum time to be in the SLOWING state before we can listen to ROLL again.
	private static final double MAX_SLOWING_TIME = 200.0;
	// Minimum Roll value, below which we consider the value calmed down.
	private static final double MIN_ROLL = 0.5;

	
	public IMURollPIDSource(IMUAdvanced imu, boolean filterData) {
		this.imu = imu; 
		this.isDataFiltered = filterData;
	}

	public boolean isDataFiltered() {
		return isDataFiltered;
	}

	public void filterData(boolean movementCorrected) {
		this.isDataFiltered = movementCorrected;
	}

	@Override
	public double pidGet() {
		encoderRate = Robot.getCurrVelocity();
		if (isDataFiltered) {
			timeinState += INTERVAL_TIME;  
			double roll = imu.getRoll();
			double velocity = Math.abs(encoderRate);
			switch (myState) {
			case rs_NORMAL: 
				// Normal operation. First check to see if we are we are going too fast.
				if (velocity < RACING_VELOCITY_THRESHOLD)
				{
					// All is good, just return the value.
					lastRoll = roll;
					return (roll - encoderRate * MAGIC_ROLL_CORRECTION_SCALAR);
				}
				// We must be racing, so set the state.
				myState = rs_RACING;
				timeInState = 0;
				// And fall on through to the racing case. (This works in C++, I assume it does in java too...)
				return roll;// It wont't fall through in Java, just wait until the next cycle
			case rs_RACING:
				// We are currently going too fast. Check to see if we've slowed down enough.
				if (velocity < RACING_VELOCITY_THRESHOLD) {
					// Dropped out of the racing state.
					myState = rs_SLOWING;
					timeinState = 0;
					return (roll - encoderRate * MAGIC_ROLL_CORRECTION_SCALAR);
				}
				// Always return a flattened Roll.
				lastRoll = roll;
				return 0;//Essentially just make the strongback happy
			case rs_SLOWING:
				// First, check to see if we've sped up enough to be back in RACING.
				if (velocity > RACING_VELOCITY_THRESHOLD) {
					// We must be racing, so set the state.
					myState = rs_RACING;
					timeInState = 0;
				}
				// We fall out of slowing either because the Roll is tiny or we've been here long enough.
				// Note that we test both that the Roll is below a minimum and that the difference between this and the last
				// roll is also below that minimum. This ensures that we aren't just transitioning across 0 with a fast 
				// moving Roll going positive or negative. That would not be a calmed state.
				else if ((timeInState > MAX_SLOWING_TIME) || ((Math.abs(roll) < MIN_ROLL) && (Math.abs(roll - lastRoll) < MIN_ROLL))) {
					myState = rs_NORMAL;
					timeinState = 0;
					lastRoll = roll;
					return roll;
				}
				lastRoll = roll;
				return 0;
			default:
				return roll;
			}
//			if (Math.abs(encoderRate) > .02)
//				System.out.println(imu.getRoll() +  " \t:\t " + encoderRate * ROLL_CORRECTION_SCALAR);
//			return (imu.getRoll() - encoderRate * MAGIC_ROLL_CORRECTION_SCALAR);// returns a value adjusted by equation found by Todor
		} else {
			return imu.getRoll(); 
		}
	}
}
 */
