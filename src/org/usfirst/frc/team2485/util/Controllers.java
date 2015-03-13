package org.usfirst.frc.team2485.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.Hashtable;

/**
 * This utility class assists in obtaining values accurately from operator
 * controls. Controller pointers must be configured before use using. 
 * Any Controllers objects not being used should be set to null. <p>     
 *  <code>Controllers.set(driver360, operator, null);</code> or <br>
 *  <code>Controllers.set(driver360, operator1, operator2);</code>  or <br> 
 *  <code>Controllers.set(driverRight, driverLeft, operator1, operator2);</code>  
 *
 * @author Bryce Matsumori
 * @author Ben Clark
 * @author Patrick Wamsley
 * 
 * @see edu.wpi.first.wpilibj.Joystick
 */
public final class Controllers {
	
	// stores controller objects
	private static Joystick	driverController 	= null,
							driverJoystickRight	= null, 
							driverJoystickLeft	= null,
							operatorJoystick1	= null, 
							operatorJoystick2 	= null;
	
	// ensure that Controllers is a static class
	private Controllers() {}
	
	/**
	 * Sets the driver, operator1, and operator2 controllers for use in
	 * {@code Controllers}. If a controller isn't used, set it as null. 
	 *
	 * @param driverController
	 *            the primary controller, such as an Xbox 360 controller
	 * @param operatorJoystick1
	 *            the secondary controller, such as a joystick
	 * @param operatorJoystick2
	 *            the tertiary controller, such as a joystick
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static void set(Joystick driverController, Joystick operatorJoystick1, Joystick operatorJoystick2) {
		Controllers.driverController	= driverController;
		Controllers.operatorJoystick1	= operatorJoystick1;
		Controllers.operatorJoystick2	= operatorJoystick2;
	}
	
	/**
	 * Sets the driver and operator controllers for use in
	 * {@code Controllers}. If a controller isn't used, set it as null. 
	 *
	 * @param driverJoystickRight
	 * @param driverJoystickLeft
	 * @param operatorJoystick1
	 * @param operatorJoystick2
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static void set(Joystick driverJoystickRight, Joystick driverJoystickLeft, 
			Joystick operatorJoystick1, Joystick operatorJoystick2) {
		
		Controllers.driverJoystickLeft 	= driverJoystickLeft; 
		Controllers.driverJoystickRight = driverJoystickRight; 
		Controllers.operatorJoystick1	= operatorJoystick1; 
		Controllers.operatorJoystick2	= operatorJoystick2; 
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
	 * Gets the current value of the specified axis on the primary controller.
	 * 
	 * @param axis
	 *            the axis
	 * @return the raw axis value (-1...1) 
	 *
	 * @throws ControllerNullException
	 *             if the primary controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getAxis(int axis) {
		return getAxis(axis, 0);
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
		if (driverController == null)
			throw new ControllerNullException("Driver controller is null");
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Xbox axis (" + axis
					+ ") is invalid.");
		
		float val = (float)driverController.getRawAxis(axis);
		
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
		if (driverController == null)
			throw new ControllerNullException("Primary controller is null");
		if (button < 1 || button > 12)
			throw new IllegalArgumentException("Xbox button number (" + button
					+ ") is invalid.");
		
		return driverController.getRawButton(button);
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
	 * Gets the current value of the specified axis on the joystick.
	 * 
	 * @param axis
	 * @return the axis value [-1, 1] 
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getOperatorJoystickOneAxis(int axis) {
		return getOperatorJoystickOneAxis(axis, 0);
	}
	/**
	 * Gets the current value of the specified axis on the joystick.
	 * 
	 * @param axis
	 * @return the axis value [-1, 1] 
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getOperatorJoystickTwoAxis(int axis) {
		return getOperatorJoystickTwoAxis(axis, 0);
	}
	
	/**
	 * Gets the current value of the specified axis on the joystick.
	 * 
	 * @param axis
	 * @return the axis value [-1, 1] 
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getDriverJoystickRightAxis(int axis) {
		return getDriverJoystickLeftAxis(axis, 0);  
	}
	
	/**
	 * Gets the current value of the specified axis on the joystick.
	 * 
	 * @param axis
	 * @return the axis value [-1, 1] 
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getDriverJoystickLeftAxis(int axis) {
		return getDriverJoystickLeftAxis(axis, 0); 
	}
	
	/**
	 * Gets the current value of the specified axis on the joystick,
	 * limited by an input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @param inputThreshold
	 *            the minimum input value allowed to indicate movement
	 * @return the axis value [-1, 1] or the input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getOperatorJoystickOneAxis(int axis, float inputThreshold) {
		if (operatorJoystick1 == null)
			throw new ControllerNullException("Secondary controller is null");
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Joystick axis (" + axis
					+ ") is invalid.");
		
		float val = (float)operatorJoystick1.getRawAxis(axis);
		
		// prevent idle movement
		if (inputThreshold > 0 && Math.abs(val) < inputThreshold)
			return 0;
		
		return val;
	}
	/**
	 * Gets the current value of the specified axis on the joystick,
	 * limited by an input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @param inputThreshold
	 *            the minimum input value allowed to indicate movement
	 * @return the axis value [-1, 1] or the input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getOperatorJoystickTwoAxis(int axis, float inputThreshold) {
		if (operatorJoystick2 == null)
			throw new ControllerNullException(
					"Secondary operator controller is null");
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Joystick axis (" + axis
					+ ") is invalid.");
		
		float val = (float)operatorJoystick2.getRawAxis(axis);
		
		// prevent idle movement
		if (inputThreshold > 0 && Math.abs(val) < inputThreshold)
			return 0;
		
		return val;
	}
	/**
	 * Gets the current value of the specified axis on the joystick,
	 * limited by an input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @param inputThreshold
	 *            the minimum input value allowed to indicate movement
	 * @return the axis value [-1, 1] or the input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getDriverJoystickRightAxis(int axis, float inputThreshold) {
		if (driverJoystickRight == null)
			throw new ControllerNullException();
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Joystick axis (" + axis
					+ ") is invalid.");

		float val = (float)driverJoystickRight.getRawAxis(axis); 

		// prevent idle movement
		if (inputThreshold > 0 && Math.abs(val) < inputThreshold)
			return 0;

		return val;

	}
	/**
	 * Gets the current value of the specified axis on the joystick,
	 * limited by an input threshold to prevent idle movement.
	 * 
	 * @param axis
	 *            the axis
	 * @param inputThreshold
	 *            the minimum input value allowed to indicate movement
	 * @return the axis value [-1, 1] or the input threshold value
	 *
	 * @throws ControllerNullException
	 *             if the controller has not been set
	 * @throws IllegalArgumentException
	 *             if the axis is not a valid axis value
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static float getDriverJoystickLeftAxis(int axis, float inputThreshold) {
		if (driverJoystickLeft == null)
			throw new ControllerNullException();
		if (axis < 0 || axis > 6)
			throw new IllegalArgumentException("Joystick axis (" + axis
					+ ") is invalid.");

		float val = (float)driverJoystickLeft.getRawAxis(axis); 

		// prevent idle movement
		if (inputThreshold > 0 && Math.abs(val) < inputThreshold)
			return 0;

		return val;

	}
	
	/**
	 * Gets the current value of the specified button on the controller.
	 * 
	 * @param button
	 * @return the button value as a boolean
	 *
	 * @throws ControllerNullException
	 *             if the joystick has not been set
	 * @throws IllegalArgumentException
	 *             if the button is not 1-12
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static boolean getOperatorJoystickOneButton(int button) {
		if (operatorJoystick1 == null)
			throw new ControllerNullException(
					"Primary operator controller is null");
		if (button < 1 || button > 22)
			throw new IllegalArgumentException("Joystick button number ("
					+ button + ") is invalid.");
		
		return operatorJoystick1.getRawButton(button);
	}
	/**
	 * Gets the current value of the specified button on the controller.
	 * 
	 * @param button
	 * @return the button value as a boolean
	 *
	 * @throws ControllerNullException
	 *             if the joystick has not been set
	 * @throws IllegalArgumentException
	 *             if the button is not 1-12
	 *
	 * @see edu.wpi.first.wpilibj.Joystick
	 */
	public static boolean getOperatorJoystickTwoButton(int button) {
		if (operatorJoystick2 == null)
			throw new ControllerNullException(
					"Secondary operator controller is null");
		if (button < 1 || button > 22)
			throw new IllegalArgumentException("Joystick button number ("
					+ button + ") is invalid");
		
		return operatorJoystick2.getRawButton(button);
	}

	
	/**
	 * Returns a {@code ControllerDataDump} containing all input data from both
	 * driver, primary operator, and secondary operator controllers.
	 *
	 * @return the data
	 */
	public static ControllerDataDump getAllData() {
		ControllerDataDump dump = new ControllerDataDump();
		
		dump.xboxAxisLX						= (float)driverController.getRawAxis(XBOX_AXIS_LX);
		dump.xboxAxisLY						= (float)driverController.getRawAxis(XBOX_AXIS_LY);
		dump.xboxAxisRX						= (float)driverController.getRawAxis(XBOX_AXIS_RX);
		dump.xboxAxisRY						= (float)driverController.getRawAxis(XBOX_AXIS_RY);
		dump.xboxAxisRTrigger 				= (float)driverController.getRawAxis(XBOX_AXIS_RTRIGGER);
		dump.xboxAxisLTrigger 				= (float)driverController.getRawAxis(XBOX_AXIS_LTRIGGER);
		
		dump.xboxBtnA						= driverController.getRawButton(XBOX_BTN_A);
		dump.xboxBtnB 						= driverController.getRawButton(XBOX_BTN_B);
		dump.xboxBtnX						= driverController.getRawButton(XBOX_BTN_X);
		dump.xboxBtnY						= driverController.getRawButton(XBOX_BTN_Y);
		dump.xboxBtnLBump					= driverController.getRawButton(XBOX_BTN_LBUMP);
		dump.xboxBtnRBump					= driverController.getRawButton(XBOX_BTN_RBUMP);
		dump.xboxBtnBack					= driverController.getRawButton(XBOX_BTN_BACK);
		dump.xboxBtnStart					= driverController.getRawButton(XBOX_BTN_START);
		dump.xboxBtnLStick					= driverController.getRawButton(XBOX_BTN_LSTICK);
		dump.xboxBtnRStick					= driverController.getRawButton(XBOX_BTN_RSTICK);
		
		dump.primaryJoystickAxisX			= (float)operatorJoystick1.getRawAxis(JOYSTICK_AXIS_X);
		dump.primaryJoystickAxisY			= (float)operatorJoystick1.getRawAxis(JOYSTICK_AXIS_Y);
		dump.primaryJoystickAxisZ			= (float)operatorJoystick1.getRawAxis(JOYSTICK_AXIS_Z);
		dump.primaryJoystickAxisThrottle	= (float)operatorJoystick1.getRawAxis(JOYSTICK_AXIS_THROTTLE);
		
		dump.secondaryJoystickAxisX			= (float)operatorJoystick2.getRawAxis(JOYSTICK_AXIS_X);
		dump.secondaryJoystickAxisY			= (float)operatorJoystick2.getRawAxis(JOYSTICK_AXIS_Y);
		dump.secondaryJoystickAxisZ			= (float)operatorJoystick2.getRawAxis(JOYSTICK_AXIS_Z);
		dump.secondaryJoystickAxisThrottle	= (float)operatorJoystick2.getRawAxis(JOYSTICK_AXIS_THROTTLE);
		
		for (int b = 0; b < 12; b++)
			dump.joystickButtons[b] = operatorJoystick1.getRawButton(b + 1);
		
		for (int b = 0; b < 12; b++)
			dump.joystickButtons[b] = operatorJoystick2.getRawButton(b + 1);
		
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