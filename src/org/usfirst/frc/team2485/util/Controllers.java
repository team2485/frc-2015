package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.Hashtable;

/**
 * This utility class assists in obtaining values accurately from operator
 * controls. Controller pointers must be configured before use using
 * {@code Controllers.set(driver, operator1, operator2)} or
 * {@code Controllers.set(driver, operator}.
 *
 * @author Bryce Matsumori
 * @author Ben Clark
 * @see edu.wpi.first.wpilibj.Joystick
 */
public final class Controllers {
	
	// stores controller objects
	private static Joystick	driver	= null, operator1 = null, operator2 = null;
	
	// ensure that Controllers is a static class
	private Controllers() {}
	
	/**
	 * Sets the driver, operator1, and operator2 controllers for use in
	 * {@code Controllers}.
	 *
	 * @param driver
	 *            the primary controller, such as an Xbox 360 controller
	 * @param operator1
	 *            the secondary controller, such as a joystick
	 * @param operator2
	 *            the tertiary controller, such as a joystick
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static void set(Joystick driver, Joystick operator1, Joystick operator2) {
		Controllers.driver		= driver;
		Controllers.operator1	= operator1;
		Controllers.operator2	= operator2;
	}
	
	public static void set(Joystick driver, Joystick operator) {
		Controllers.driver		= driver;
		Controllers.operator1	= operator;
		Controllers.operator2	= null;
	}
	
	/**
	 * The default input threshold value.
	 */
	public static final float DEFAULT_INPUT_THRESH = 0.3f;
	
	/**
	 * The driver Xbox 360 controller left Y-axis. See {@link http
	 * ://www.chiefdelphi.com/forums/showpost.php?p=1003245&postcount=8}.
	 */
	
	public static final int		XBOX_AXIS_LX			= 0;
	
	public static final int		XBOX_AXIS_LY			= 1;
	/**
	 * The primary Xbox 360 controller left trigger. Values range from 0.0 to
	 * 1.0.
	 */
	public static final int		XBOX_AXIS_LTRIGGER		= 2;
	/**
	 * The primary Xbox 360 controller right trigger. Values range from 0.0 to
	 * 1.0.
	 */
	public static final int		XBOX_AXIS_RTRIGGER		= 3;
	/**
	 * The primaryXbox 360 controller right X-axis.
	 */
	public static final int		XBOX_AXIS_RX			= 4;
	/**
	 * The primary Xbox 360 controller right Y-axis.
	 */
	public static final int		XBOX_AXIS_RY			= 5;
	

	/**
	 * The driver Xbox 360 controller A (south) button. See {@link http
	 * ://www.chiefdelphi.com/forums/showpost.php?p=1003245&postcount=8}.
	 */
	public static final int		XBOX_BTN_A				= 1;
	/**
	 * The primary Xbox 360 controller B (east) button.
	 */
	public static final int		XBOX_BTN_B				= 2;
	/**
	 * The primary Xbox 360 controller X (west) button.
	 */
	public static final int		XBOX_BTN_X				= 3;
	/**
	 * The primary Xbox 360 controller Y (north) button.
	 */
	public static final int		XBOX_BTN_Y				= 4;
	/**
	 * The primary Xbox 360 controller left bumper.
	 */
	public static final int		XBOX_BTN_LBUMP			= 5;
	/**
	 * The primary Xbox 360 controller right bumper.
	 */
	public static final int		XBOX_BTN_RBUMP			= 6;
	/**
	 * The primary Xbox 360 controller and Guitar Hero X-plorer back button.
	 */
	public static final int		XBOX_BTN_BACK			= 7;
	/**
	 * The primary Xbox 360 controller and Guitar Hero X-plorer start button.
	 */
	public static final int		XBOX_BTN_START			= 8;
	/**
	 * The primary Xbox 360 controller left joystick (pressed).
	 */
	public static final int		XBOX_BTN_LSTICK			= 9;
	/**
	 * The primary Xbox 360 controller right joystick (pressed).
	 */
	public static final int		XBOX_BTN_RSTICK			= 10;
	
	
	/**
	 * Gets the current value of the specified axis on the primary controller,
	 * limited by the default input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @return the axis value (-1...1) or the default input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the primary controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getAxis(int axis) {
		return getAxis(axis, DEFAULT_INPUT_THRESH);
	}
	
	/**
	 * Gets the current value of the specified axis on the driver controller,
	 * limited by an input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @param inputThreshold
	 *            the minimum input value allowed to indicate movement
	 * @return the axis value (-1...1) or the input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the primary controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getAxis(int axis, float inputThreshold) {
		if (driver == null)
			throw new ControllerNullException("Driver controller is null");
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Xbox axis (" + axis
					+ ") is invalid.");
		
		float val = (float)driver.getRawAxis(axis);
		
		// prevent idle movement
		if (inputThreshold > 0 && Math.abs(val) < inputThreshold)
			return 0;
		
		return val;
	}
	
	/**
	 * Gets the current value of the specified button on the primary controller.
	 * 
	 * @param button
	 *            the button
	 * @return the button value as a boolean
	 *
	 * @throws ControllerNullException
	 *             if the primary controller has not been set
	 * @throws IllegalArgumentException
	 *             if the button is not a valid button value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static boolean getButton(int button) {
		if (driver == null)
			throw new ControllerNullException("Primary controller is null");
		if (button < 1 || button > 12)
			throw new IllegalArgumentException("Xbox button number (" + button
					+ ") is invalid.");
		
		return driver.getRawButton(button);
	}
	
	
	/**
	 * The operator joystick X (horizontal) axis.
	 */
	public static final int	JOYSTICK_AXIS_X			= 0;
	/**
	 * The operator joystick Y (vertical) axis.
	 */
	public static final int	JOYSTICK_AXIS_Y			= 1;
	/**
	 * The operator joystick Z (twist) axis.
	 */
	public static final int	JOYSTICK_AXIS_Z			= 2;
	/**
	 * The operator joystick throttle axis
	 */
	public static final int	JOYSTICK_AXIS_THROTTLE	= 3;
	
	
	/**
	 * Gets the current value of the specified axis on the secondary controller,
	 * limited by the default input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @return the axis value (-1...1) or the default input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the secondary controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getJoystickAxis(int axis) {
		return getJoystickAxis(axis, DEFAULT_INPUT_THRESH);
	}
	
	public static float getSecondaryJoystickAxis(int axis) {
		return getSecondaryJoystickAxis(axis, DEFAULT_INPUT_THRESH);
	}
	
	/**
	 * Gets the current value of the specified axis on the secondary controller,
	 * limited by an input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @param inputThreshold
	 *            the minimum input value allowed to indicate movement
	 * @return the axis value (-1...1) or the input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the secondary controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getJoystickAxis(int axis, float inputThreshold) {
		if (operator1 == null)
			throw new ControllerNullException("Secondary controller is null");
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Joystick axis (" + axis
					+ ") is invalid.");
		
		float val = (float)operator1.getRawAxis(axis);
		
		// prevent idle movement
		if (inputThreshold > 0 && Math.abs(val) < inputThreshold)
			return 0;
		
		return val;
	}
	
	public static float getSecondaryJoystickAxis(int axis, float inputThreshold) {
		if (operator2 == null)
			throw new ControllerNullException(
					"Secondary operator controller is null");
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Joystick axis (" + axis
					+ ") is invalid.");
		
		float val = (float)operator2.getRawAxis(axis);
		
		// prevent idle movement
		if (inputThreshold > 0 && Math.abs(val) < inputThreshold)
			return 0;
		
		return val;
	}
	
	/**
	 * Gets the current value of the specified button on the secondary
	 * controller.
	 * 
	 * @param button
	 *            a button 1-22
	 * @return the button value as a boolean
	 *
	 * @throws ControllerNullException
	 *             if the secondary controller has not been set
	 * @throws IllegalArgumentException
	 *             if the button is not 1-12
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static boolean getJoystickButton(int button) {
		if (operator1 == null)
			throw new ControllerNullException(
					"Primary operator controller is null");
		if (button < 1 || button > 22)
			throw new IllegalArgumentException("Joystick button number ("
					+ button + ") is invalid.");
		
		return operator1.getRawButton(button);
	}
	
	public static boolean getSecondaryJoystickButton(int button) {
		if (operator2 == null)
			throw new ControllerNullException(
					"Secondary operator controller is null");
		if (button < 1 || button > 22)
			throw new IllegalArgumentException("Joystick button number ("
					+ button + ") is invalid");
		
		return operator2.getRawButton(button);
	}

	
	/**
	 * Returns a {@code ControllerDataDump} containing all input data from both
	 * driver, primary operator, and secondary operator controllers.
	 *
	 * @return the data
	 */
	public static ControllerDataDump getAllData() {
		ControllerDataDump dump = new ControllerDataDump();
		
		dump.xboxAxisLX						= (float)driver.getRawAxis(XBOX_AXIS_LX);
		dump.xboxAxisLY						= (float)driver.getRawAxis(XBOX_AXIS_LY);
		dump.xboxAxisRX						= (float)driver.getRawAxis(XBOX_AXIS_RX);
		dump.xboxAxisRY						= (float)driver.getRawAxis(XBOX_AXIS_RY);
		dump.xboxAxisRTrigger 				= (float)driver.getRawAxis(XBOX_AXIS_RTRIGGER);
		dump.xboxAxisLTrigger 				= (float)driver.getRawAxis(XBOX_AXIS_LTRIGGER);
		
		dump.xboxBtnA						= driver.getRawButton(XBOX_BTN_A);
		dump.xboxBtnB 						= driver.getRawButton(XBOX_BTN_B);
		dump.xboxBtnX						= driver.getRawButton(XBOX_BTN_X);
		dump.xboxBtnY						= driver.getRawButton(XBOX_BTN_Y);
		dump.xboxBtnLBump					= driver.getRawButton(XBOX_BTN_LBUMP);
		dump.xboxBtnRBump					= driver.getRawButton(XBOX_BTN_RBUMP);
		dump.xboxBtnBack					= driver.getRawButton(XBOX_BTN_BACK);
		dump.xboxBtnStart					= driver.getRawButton(XBOX_BTN_START);
		dump.xboxBtnLStick					= driver.getRawButton(XBOX_BTN_LSTICK);
		dump.xboxBtnRStick					= driver.getRawButton(XBOX_BTN_RSTICK);
		
		dump.primaryJoystickAxisX			= (float)operator1.getRawAxis(JOYSTICK_AXIS_X);
		dump.primaryJoystickAxisY			= (float)operator1.getRawAxis(JOYSTICK_AXIS_Y);
		dump.primaryJoystickAxisZ			= (float)operator1.getRawAxis(JOYSTICK_AXIS_Z);
		dump.primaryJoystickAxisThrottle	= (float)operator1.getRawAxis(JOYSTICK_AXIS_THROTTLE);
		
		dump.secondaryJoystickAxisX			= (float)operator2.getRawAxis(JOYSTICK_AXIS_X);
		dump.secondaryJoystickAxisY			= (float)operator2.getRawAxis(JOYSTICK_AXIS_Y);
		dump.secondaryJoystickAxisZ			= (float)operator2.getRawAxis(JOYSTICK_AXIS_Z);
		dump.secondaryJoystickAxisThrottle	= (float)operator2.getRawAxis(JOYSTICK_AXIS_THROTTLE);
		
		for (int b = 0; b < 12; b++)
			dump.joystickButtons[b] = operator1.getRawButton(b + 1);
		
		for (int b = 0; b < 12; b++)
			dump.joystickButtons[b] = operator2.getRawButton(b + 1);
		
		return dump;
	}
	
	/**
	 * Stores all the input data from both primary and secondary controllers.
	 *
	 * @see Controllers
	 */
	public static final class ControllerDataDump {
		
		public float		xboxAxisLX, xboxAxisLY, xboxAxisRX, xboxAxisRY,
				xboxAxisRTrigger, xboxAxisLTrigger, xboxAxisDPad;
		public boolean		xboxBtnA, xboxBtnB, xboxBtnX, xboxBtnY,
				xboxBtnLBump, xboxBtnRBump, xboxBtnBack, xboxBtnStart,
				xboxBtnLStick, xboxBtnRStick;
		
		public float		primaryJoystickAxisX, primaryJoystickAxisY,
				primaryJoystickAxisZ, primaryJoystickAxisThrottle,
				secondaryJoystickAxisX, secondaryJoystickAxisY,
				secondaryJoystickAxisZ, secondaryJoystickAxisThrottle;
		public boolean[]	joystickButtons	= new boolean[12];
	}
	
	
	/**
	 * Thrown when an application attempts to use the {@code Controllers} class
	 * without first setting the controllers.
	 *
	 * @author Bryce Matsumori
	 * @see Controllers
	 * @see java.lang.NullPointerException
	 */
	public static class ControllerNullException extends NullPointerException {
		
		/**
		 * Constructs a {@code ControllerNullException} with no detail message.
		 */
		public ControllerNullException() {
		}
		
		/**
		 * Constructs a {@code ControllerNullException} with the specified
		 * detail message.
		 * 
		 * @param s
		 *            the detail message
		 */
		public ControllerNullException(String s) {
			super(s);
		}
	}
}