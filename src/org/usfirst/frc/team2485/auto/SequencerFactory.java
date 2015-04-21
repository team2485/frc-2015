package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.auto.SequencedItems.*;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.*;

/**
 * 
 * Contains methods which return Sequencer objects in order to run the robot autonomously, 
 * during both the auto and teleop modes of the game. 
 * 
 * @author Patrick Wamsley
 * @author Anoushka Bose
 * @author Aidan Fay
 * @author Ben Clark
 */

public class SequencerFactory {

	// auto types
	public static final int  
			DRIVE_TO_AUTO_ZONE = 0, 
			ONE_CONTAINER = 1,
			CONTAINER_STEAL = 2,
			THREE_TOTE = 3,
			DO_NOTHING = 4;
	
			
	public static Sequencer createAuto(int autoType) {

		switch (autoType) {
		
			case DO_NOTHING:
				return new Sequencer(new SequencedItem[] {
						new SequencedPause(0.1),
						new MoveClawVertically(Claw.ONE_AND_TWO_TOTE_RESTING_POS)
				});
			
			case DRIVE_TO_AUTO_ZONE:
				return new Sequencer( new SequencedItem[] {
						new DriveStraight(70),
						new DisableDriveStraightPID()
				});

			case ONE_CONTAINER:
				return new Sequencer(new SequencedItem[] {
						new OpenClapper(), 
						new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
						new CloseClaw(),
						new MoveClawVertically(Claw.ONE_AND_TWO_TOTE_RESTING_POS),
						new MoveClawConstantSpeed(0), 
						new RotateToAngle(-90),
						new DisableIMUPID(),
						new ResetDriveEncoders(),
						new SetDriveStraightPID(DriveTrain.driveStraightEncoder_ONE_CONTAINER_Kp),
						new DriveStraight(130),
						new DisableDriveStraightPID()
				});
				
			case CONTAINER_STEAL:
				return new Sequencer(new SequencedItem[] {
						new DriveAtSetSpeed(-0.4, 0.03),
						new CommandeerContainerSequence(CommandeerContainerSequence.BOTH),
						new SequencedPause(.05),
						new DriveAtSetSpeed(0, 0.03),
						new SequencedPause(.85), 
						new CommandeerContainerSequence(CommandeerContainerSequence.RETRACT_BOTH),
						new DriveStraight(60),
						new DisableDriveStraightPID(),
						new SequencedPause(1),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
						new SequencedPause(2),
						new OpenClapper(),
						new RetractRatchet(),
						new MoveClawVertically(Claw.LOWEST_POS)
				});
			case THREE_TOTE: 
				double MIN_TIME_BETWEEN_MOVEMENT_TYPES = .05;
				double ANGLE_TO_ROTATE_AT_END = -80;
				double offsetToWall = Robot.getAutoWallSonicOffset();
				return new Sequencer(new SequencedItem[] {
						
						//Lift tote and drive around container dance...starts here...
						new SequencedMultipleItem(
								new CloseClapper(),
								new DropCenterWheel(true)
							),
						new StrafeTo(offsetToWall + 25, 1.45, 0), 
						new SequencedMultipleItem(
								new DisableStrafePID(),
								new ResetLastStrafeValue(),
								new ResetDriveEncoders(), 
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
							),
						new SequencedMultipleItem(	
								new DropCenterWheel(false),
								new DriveStraight(40, 1.25, 0),
								new InnerSequencer(createAutoToteLiftRoutinePartOne()) 
							),
						new SequencedMultipleItem(
								new DisableDriveStraightPID(),
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
							),
						new SequencedMultipleItem(
								new StrafeTo(offsetToWall + 8, 1.0, 0), // center up...the non-zero # compensates for error
								new InnerSequencer(createAutoToteLiftRoutinePartTwo()) 
							),
						//now drive up to the next tote, clappers open, rollers running
						new SequencedMultipleItem(
								new ResetLastStrafeValue(),
								new ResetDriveEncoders(), 
								new DisableStrafePID(), 
								new OpenClapper(), 
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES),
								new RunRollers(.8),
								new DropCenterWheel(false)
							),
						new DriveStraight(40, 3, 0), 
						//close the clappers, letting the rollers run still for just a bit
						new SequencedMultipleItem(
								new CloseClapper(), 
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES + 0.25), //let the rollers run a bit longer...the extra addition
								new DisableDriveStraightPID()
							), 
						new RunRollers(0),
							
						//Lift tote and drive around container dance again. 
						new StrafeTo(Robot.getAutoWallSonicOffset() + 25, 1.35, 0),  
						new SequencedMultipleItem( 
								new ResetLastStrafeValue(),
								new DisableStrafePID(),
								new ResetDriveEncoders(), 
								new DropCenterWheel(false),
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
							),
						new SequencedMultipleItem(
								new DriveStraight(35, 1.25, 0), 
								new InnerSequencer(createAutoToteLiftRoutinePartOne())
							),
						new SequencedMultipleItem(
								new DisableDriveStraightPID(),
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
							),	
						new SequencedMultipleItem(
								new StrafeTo(Robot.getAutoWallSonicOffset() + 8, 1.0, 0), //center up...non-zero # is to compensate for error
								new InnerSequencer(createAutoToteLiftRoutinePartTwo())
							),
						//now drive up to the next tote, clappers open, rollers running
						new SequencedMultipleItem(
								new DisableStrafePID(), 
								new ResetLastStrafeValue(),
								new ResetDriveEncoders(), 
								new OpenClapper(), 
								new DropCenterWheel(false),
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES),
								new RunRollers(.8)
							),
						new DriveStraight(55, 3, 0), 
						
						//close the clappers, letting the rollers run still for just a bit
						new SequencedMultipleItem(
								new CloseClapper(), 
								new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES + .25), //let the rollers run a bit longer...the extra addition
								new DisableDriveStraightPID()
							), 
						
						//end of the sequence
							
						//rollers are still running...see above
						new DropCenterWheel(false),
						new DisableStrafePID(),
						new DisableDriveStraightPID(),
						new RotateToAngle(ANGLE_TO_ROTATE_AT_END, 1.25),
						new ResetDriveEncoders(),
						new DriveBackAndDropTotesForAuto(-1, 3), //encoderes were returning a positive error; proof of concept here is solid. 
						new RunRollers(0), 
						new DriveAtSetSpeed(-.3, .03), 
						new SequencedPause(.25), 
						new DriveAtSetSpeed(0, .3), 
				}); 
				//end of switch statement
		}
		return new Sequencer();
	}
	

	/**
	 * @return Sequence used to lift a tote and hang it on the hook, after the tote is fully inside the clappers (after intake). 
	 */
	public static Sequencer createToteLiftRoutine() {
			return new Sequencer(new SequencedItem[] {
						new ExtendRatchet(), 
						new RunRollers(0.6),
						new IncrementToteCount(),
						new RunRollers(0),
						new SetClapperPIDByToteCount(),
						new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
						new SequencedMultipleItem(
								new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
								new MoveClawWithClapper(MoveClawWithClapper.UP)
						), 
						new MoveClawConstantSpeed(0),
						new SequencedPause(.25),
						new SetClapperPID(0.0005, 0, 0),
						new SequencedMultipleItem(
								new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT),
								new MoveClawWithClapper(MoveClawWithClapper.DOWN)
								),
						new MoveClawConstantSpeed(0),
						new SetClapperPIDByToteCount(),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT)
				});
	}
	
	
	/**
	 * @return Sequence which lifts a tote above the ratchet and then holds it there. Used in 3-tote auto.
	 */
	private static Sequencer createAutoToteLiftRoutinePartOne() {
		return new Sequencer(
				new SequencedItem[] {
						new SequencedMultipleItem(
							new CloseClapper(),
							new ExtendRatchet(), 
							new RunRollers(0.6),
							new IncrementToteCount()
						),
						new RunRollers(0),
						new SetClapperPIDByToteCount(),
						new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
				});
	}
	
	/**
	 * @return Sequence that hangs a tote on the ratchet after already being lifted by the "PartOne" sequence. It
	 * is designed to use in our 3-tote auto. It also stops the rollers from moving.
	 */
	private static Sequencer createAutoToteLiftRoutinePartTwo() {
		return new Sequencer(
				new SequencedItem[] {
						new SequencedMultipleItem(
								new RunRollers(0),
								new SetClapperPID(0.0005, 0, 0),
								new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT, .25)
							),
						new SetClapperPIDByToteCount(),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT, 0.03)

				});
	}
	
	/**
	 * @return sequence that drops a tote stack and a container 
	 */ 
	public static Sequencer createDropToteStackRoutine(boolean toteBelowRatchet) { 
		Sequencer returnSequence = null;

		double clawSetpoint = Claw.HIGHEST_POS - (Claw.POTS_PER_INCH * .5);
		int toteCount = Robot.toteCounter.getCount();
		
		// When dropping a stack with tote under ratchet, tote count is one less than what we "actually" have
		// In order for the claw to go to the correct setpoint we have to "pretend" tote count is more
		//If we don't have a tote underneath, subtracting one from tote count to get it to the right claw setpoint

		if (!toteBelowRatchet)
			toteCount--;
		
		switch (toteCount) {
			
			case 0:
				clawSetpoint = Claw.DROP_SEQ_POS_1;
				break;
			case 1: 
				clawSetpoint = Claw.DROP_SEQ_POS_2; 
				break; 
			case 2: 
				clawSetpoint = Claw.DROP_SEQ_POS_3;
				break; 
			case 3:
				clawSetpoint = Claw.DROP_SEQ_POS_4;
				break;
			case 4:
				clawSetpoint = Claw.DROP_SEQ_POS_5;
				break;
			case 5:
				clawSetpoint = Claw.DROP_SEQ_POS_6;
				break;
		
		}
		
		if (toteBelowRatchet) {		
			returnSequence = new Sequencer(
					new SequencedItem[] {
							new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
							new CloseClapper(),
							new RetractRatchet(),
							new SetClapperPIDByToteCount(),
							new SequencedMultipleItem(
									new MoveClapperVertically(
											Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT, 1.5),
									new MoveClawVertically(Claw.HIGHEST_POS - Claw.POTS_PER_INCH * .5
											- ((5 - Robot.toteCounter.getCount()) * (Claw.POTS_PER_INCH * 12)))
									),
							new SequencedPause(.25), 
							new OpenClaw(), 
							new SequencedMultipleItem(
									new MoveClapperVertically(Clapper.LOADING_SETPOINT),
									new MoveClawVertically(clawSetpoint)
									),
							new OpenClapper(),
							new SequencedPause(.1), 
							new MoveClawVertically(Robot.claw.getPotValue() + 15) 
						});
			
		} else {
			// assumption is that all of the totes are on the hook...start by
			// making sure that the clapper is in the correct position
			returnSequence = new Sequencer(new SequencedItem[] {
					new SetClawPID(Claw.kP_STANDARD, Robot.claw.getI(), Robot.claw.getD()),
					new SequencedMultipleItem(
							new OpenClapper(),
							new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT)
						),
					new CloseClapper(),
					new SequencedMultipleItem(
							new MoveClapperVerticallyForToteDrop(Clapper.ABOVE_RATCHET_SETPOINT + 20),
							new MoveClawVertically(Claw.HIGHEST_POS - Claw.POTS_PER_INCH * .5
									- ((5 - Robot.toteCounter.getCount()) * (Claw.POTS_PER_INCH * 12)))
								),					
					new RetractRatchet(),
					new OpenClaw(),
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.LOADING_SETPOINT), 
							new MoveClawVertically(clawSetpoint)),
					new OpenClapper(),
					new SequencedPause(.1), 
					new MoveClawVertically(Robot.claw.getPotValue() + 15)
				});
		}
		Robot.toteCounter.resetCount();
		return returnSequence;
	}
	
	/**
	 * @param Number of totes on the step
	 * @return Sequence that prepares the clappers and claw to place a stack on Coop. Step. 
	 */
	public static Sequencer createPrepareCoopertitionStack(int numberOfTotesOnStep) {
		
		double clapperSetpoint = 0;
		
		switch (numberOfTotesOnStep) {
			case 0:
				clapperSetpoint = Clapper.ABOVE_STEP_SETPOINT;
				break;
			case 1:
				clapperSetpoint = Clapper.ABOVE_STEP_SETPOINT + Clapper.TOTE_HEIGHT * 1;
				break;
			case 2:
				clapperSetpoint = Clapper.ABOVE_STEP_SETPOINT + Clapper.TOTE_HEIGHT * 2;
				break;
		}
		
		return new Sequencer(new SequencedItem[] {
				new RetractRatchet(),
				new SequencedMultipleItem(
						new MoveClapperVertically(clapperSetpoint),
						new MoveClawVertically(Claw.HIGHEST_POS - 10)
				)
		});
	}
	
	/**
	 * @return Sequence that places Coop. tote stack. 
	 */
	public static Sequencer createCoopertitionStackRoutine() {
		double adjustmentCoopValue = Clapper.POTS_PER_INCH * 3;
		double setpoint = Robot.clapper.getPotValue() - adjustmentCoopValue;
		
		return new Sequencer(new SequencedItem[] {
				new MoveClapperVertically(setpoint),
				new ResetDriveEncoders(),
				new SequencedMultipleItem(
						new DriveStraight(5),
						new RunRollers(-0.7)
				),
				new RunRollers(0),
				new OpenClapper()
		});
	}

	/**
	 * @return Sequence which rights a fallen over container using the clappers. 
	 */
	public static Sequencer createContainerRightingRoutine() {
		
		if (Robot.toteCounter.getCount() != 0)
			return null; 
		
		return new Sequencer(new SequencedItem[] {
				new CloseClapper(), 
				new SetClapperPID(Clapper.kP_DEFAULT, 0, 0),
				new SequencedPause(.1), 
				new MoveClapperVertically(Clapper.RIGHTING_CONTAINER_POS),
				new SequencedPause(.25), //this one is actually probably needed
				new OpenClapper()
		});
	}
	
	/**
	 * @return a Sequence which prepares the clapper and claw for righting a container. 
	 */
	public static Sequencer createPrepareForContainerRightingRoutine() {
		
		if (Robot.toteCounter.getCount() != 0) 
			return null;
		
		return new Sequencer(new SequencedItem[] {
				new SetClawPID(Claw.kP_STANDARD, Robot.claw.getI(), Robot.claw.getD()),
				new SetClapperPID(Clapper.kP_DEFAULT, 0, 0),
				new SequencedMultipleItem(
						new CloseClaw(),
						new MoveClawVertically(Claw.ONE_AND_TWO_TOTE_RESTING_POS),
						new OpenClapper(),
						new MoveClapperVertically(Clapper.RIGHTING_CONTAINER_PRE_POS)
					)

		});
	}

	/**
	 * @return Sequence which pickups a container an then raises claw. Assumes the claw in in position and container is not fallen over. 
	 */
	public static Sequencer createContainerPickupRoutine() {

		if (Robot.toteCounter.getCount() != 0)
			return null; 

		return new Sequencer(new SequencedItem[] {
				new SetClawPID(Claw.kP_STANDARD, Robot.claw.getI(), Robot.claw.getD()),
				new CloseClaw(),
				new SequencedPause(.1),
				new MoveClawVertically(Claw.ONE_AND_TWO_TOTE_RESTING_POS),//see comment
				new CloseClapper(),
		});
	}

	/**
	 * @return Sequence which will move the position of the claw on the container in order to make larger stacks. <br> 
	 * Will only run if tote count is 2. 
	 */
	public static Sequencer createAdjustClawOnContainerRoutine() {
		if (Robot.toteCounter.getCount() != 2)
			return null;

		return new Sequencer(new SequencedItem[] {
				new MoveClawVertically(Claw.CONTAINER_ADJUSTMANT_POS),
				new SequencedPause(.25), 
				new OpenClaw(), 
				new MoveClawVertically(Claw.TWO_TOTE_PLACEMENT_POS), 
				new CloseClaw(),
				new SequencedPause(.05),
				new MoveClawVertically(Claw.TWO_TOTE_PLACEMENT_POS + Claw.POTS_PER_INCH * 4),
				new SequencedPause(.05),
				new MoveClawVertically(Claw.TWO_TOTE_PLACEMENT_POS + Claw.POTS_PER_INCH * 1)
		}); 
	}
	
	/**
	 * @return Sequence which prepares the Clapper and Claw to pickup a container which has not fallen over. 
	 */
	public static Sequencer createPrepareForContainerLiftRoutine() {

		if (Robot.toteCounter.getCount() != 0)
			return null; 
		
		return new Sequencer(new SequencedItem[] { 
				new OpenClapper(), 
				new CloseClaw(),
				new RetractRatchet(), 
				new SetClawPID(Claw.kP_STANDARD, Robot.claw.getI(), Robot.claw.getD()),		
				new SequencedMultipleItem(						
						new MoveClawVertically(Claw.LOWEST_POS + 5),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT)
				),
				new OpenClaw()
			});
	}
}
