package org.usfirst.frc.team2485.auto;

import javax.swing.text.Highlighter;

import org.usfirst.frc.team2485.auto.SequencedItems.CloseClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.CommandeerContainerSequence;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableDriveStraightPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableIMUPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableStrafePID;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableStrongbackPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveAtSetSpeed;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraight;
import org.usfirst.frc.team2485.auto.SequencedItems.DropCenterWheel;
import org.usfirst.frc.team2485.auto.SequencedItems.ExtendRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.IncrementToteCount;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClapperConstantSpeed;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClapperVertically;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClapperVerticallyForToteDrop;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawConstantSpeed;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawVertically;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawWithClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.OpenClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.OpenClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.ResetDriveEncoders;
import org.usfirst.frc.team2485.auto.SequencedItems.ResetLastStrafeValue;
import org.usfirst.frc.team2485.auto.SequencedItems.RetractRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.RotateToAngle;
import org.usfirst.frc.team2485.auto.SequencedItems.RunRollers;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClapperPID;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClapperPIDByToteCount;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClawPID;
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingersPos;
import org.usfirst.frc.team2485.auto.SequencedItems.SetRollers;
import org.usfirst.frc.team2485.auto.SequencedItems.StrafeTo;
import org.usfirst.frc.team2485.auto.SequencedItems.StrafeToWithoutMaintainingHeading;
import org.usfirst.frc.team2485.auto.SequencedItems.TiltStrongback;
import org.usfirst.frc.team2485.auto.SequencedItems.ToteIntake;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Clapper;
import org.usfirst.frc.team2485.subsystems.Claw;
import org.usfirst.frc.team2485.subsystems.Fingers;
import org.usfirst.frc.team2485.subsystems.Strongback;

public class SequencerFactory {

	// auto types
	public static final int  
			DRIVE_TO_AUTO_ZONE = 0, 
			ONE_CONTAINER = 1,
			CONTAINER_STEAL = 2,
			THREE_TOTE = 3;
	
	
	public static final int STRAFE_TESTING = 222222; 
			
	public static Sequencer createAuto(int autoType) {

		switch (autoType) {
		
		case DRIVE_TO_AUTO_ZONE:
			return new Sequencer( new SequencedItem[] {
					new DriveStraight(70),
					new DisableDriveStraightPID()
			});
			
		case STRAFE_TESTING:
			return new Sequencer( new SequencedItem[] {
					new ResetDriveEncoders(),
					new StrafeTo(-25),
					new DisableStrafePID(),
					new SequencedPause(.1),
					new DriveStraight(25),
					new SequencedPause(.5),
					new DisableDriveStraightPID(),
					new DisableStrafePID(),
//					new ResetDriveEncoders(),
					new StrafeTo(0),
					new SequencedPause(1),
					new DriveStraight(50),
					new DisableStrafePID(),
					new DisableDriveStraightPID(),
//					new ResetDriveEncoders()
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
					new DriveStraight(60),
					new DisableDriveStraightPID()
			});
			
		case CONTAINER_STEAL:
			return new Sequencer(new SequencedItem[] {
//					new TiltStrongback(0),
//					new SequencedPause(1),
					new CommandeerContainerSequence(CommandeerContainerSequence.BOTH),
					new SequencedPause(.85), //TODO: how long does the pause need to be?
					new CommandeerContainerSequence(CommandeerContainerSequence.RETRACT_BOTH),
					new DriveStraight(90),
					new DisableDriveStraightPID(),
					new SequencedPause(1),
					new TiltStrongback(Strongback.STANDARD_SETPOINT),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new SequencedPause(2),
					new DisableStrongbackPID(),
//					new DriveStraight(-20),
//					new DisableDriveStraightPID(),
					new OpenClapper(),
					new RetractRatchet(),
					new MoveClawVertically(Claw.LOWEST_POS)
//					new RotateToAngle(angle)
			});
		case THREE_TOTE: 
			double MIN_TIME_BETWEEN_MOVEMENT_TYPES = .25;
			double ANGLE_TO_ROTATE_AT_END = -80;
			return new Sequencer(new SequencedItem[] {
					
					//Lift tote and drive around container dance...starts here...
					new SequencedMultipleItem(
							new InnerSequencer(createAutoToteLiftRoutinePartOne()), 
							new StrafeTo(40, 1.25, 0) //distance untested
						), 
					new SequencedMultipleItem(
//							new RunRollers(.4), 
							new DisableStrafePID(),
							new ResetLastStrafeValue(),
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
						),
					new SequencedMultipleItem(	
							new DriveStraight(50, 1.25, 0), //distance untested
							new InnerSequencer(createAutoToteLiftRoutinePartTwo())
						),
					new SequencedMultipleItem(
							new DisableDriveStraightPID(),
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
						),
					new StrafeTo(20, 1.25, 0), // center up...the non-zero # compensates for error
					
					//now drive up to the next tote, clappers open, rollers running
					new SequencedMultipleItem(
							new ResetLastStrafeValue(),
							new DisableStrafePID(), 
							new OpenClapper(), 
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES),
							new RunRollers(.8)
						),
					new DriveStraight(100, 3, 0), //distance untested
					
					//close the clappers, letting the rollers run still for just a bit
					new SequencedMultipleItem(
							new CloseClapper(), 
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES + 0.5), //let the rollers run a bit longer...the extra addition
							new DisableDriveStraightPID()
						), 
					new RunRollers(0),
					
					/////////////////////////////////////////////////////////////////////
					/////////////////////////////////////////////////////////////////////
					//Now do the lift tote and drive around container dance again
					/////////////////////////////////////////////////////////////////////
					/////////////////////////////////////////////////////////////////////	
					
					//Lift tote and drive around container dance...starts here...
					new SequencedMultipleItem(
							new InnerSequencer(createAutoToteLiftRoutinePartOne()), 
							new StrafeTo(45, 1.25, 0) //distance untested
						), 
					new SequencedMultipleItem(
//							new RunRollers(.4), 
							new ResetLastStrafeValue(),
							new DisableStrafePID(), 
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
						),

					new InnerSequencer(createAutoToteLiftRoutinePartTwo()),
					
					
					
					new DriveStraight(150, 1.25, 0), //distance untested
							

					new SequencedMultipleItem(
							new DisableDriveStraightPID(),
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES)
						),
					new StrafeTo(15, 1.25, 0), //center up...non-zero # is to compensate for error
					
					//now drive up to the next tote, clappers open, rollers running
					new SequencedMultipleItem(
							new DisableStrafePID(), 
							new ResetLastStrafeValue(),
							new OpenClapper(), 
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES),
							new RunRollers(.8)
						),
					new DriveStraight(200, 3, 0), //distance untested
					
					//close the clappers, letting the rollers run still for just a bit
					new SequencedMultipleItem(
							new CloseClapper(), 
							new SequencedPause(MIN_TIME_BETWEEN_MOVEMENT_TYPES + .25), //let the rollers run a bit longer...the extra addition
							new DisableDriveStraightPID()
						), 
					
						//////////////////////////////////////////
					/////////////////////////////////////////////////
					//START OF END SEQUENCE
					/////////////////////////////////////////////////
						/////////////////////////////////////////
					
					//rollers are still running...see above
					new DropCenterWheel(false),
					new MoveClapperVertically(Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT + 20, 0.1),
					new RotateToAngle(ANGLE_TO_ROTATE_AT_END, 1),
					new ResetDriveEncoders(),
//					new MoveClapperConstantSpeed(.6, .1),
					new SequencedMultipleItem(
							new DriveStraight(-150, 1.5, ANGLE_TO_ROTATE_AT_END),
							new RetractRatchet()
						),
					new DriveAtSetSpeed(0, 0.03),
					new SequencedPause(.2),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT, .2),
					new OpenClapper(),
					new DriveStraight(-170, 0.75, ANGLE_TO_ROTATE_AT_END),
//					new SequencedMultipleItem(
//							new DriveStraight(-150, 1, ANGLE_TO_ROTATE_AT_END),
//							new MoveClapperVertically(Clapper.LOADING_SETPOINT, 1),
//							new RunRollers(-0.8)
//							),
//					new SequencedMultipleItem(
//							new DriveStraight(-160, 1, ANGLE_TO_ROTATE_AT_END),
//							new OpenClapper()
//							),
					new RunRollers(0),
					new DriveAtSetSpeed(0, .1),
					 
					
					//Now we should have all three totes...lift and drive or just drive with the third one on the floor?
					
//					new InnerSequencer(createToteLiftRoutine()), //pickup second tote 
//					new StrafeTo(-30, .75), //distance untested 
//					new DisableStrafePID(),
//					new SequencedPause(.1), 
//					new DriveStraight(130, 1), //distance untested but cumulative and relative to starting 0
//					new DisableDriveStraightPID(),
//					new SequencedPause(.1), 
//					new StrafeTo(0), 
//					new DisableStrafePID(), 
//					new SequencedPause(.1), 
//					new OpenClapper(), 
//					new SequencedMultipleItem(
//							new DriveStraight(150), //distance untested
//							new RunRollers(.8)
//						), 
//					new CloseClapper(), 
//					new SequencedPause(.25), //let the rollers run for a bit longer
//					new RunRollers(0),
//					new DisableDriveStraightPID(), 
//					new InnerSequencer(createToteLiftRoutine()), //3rd tote :D
////					new StrafeTo(-60), //maybe should use rotate and drive
//					new RotateToAngle(-90),
//					new DisableIMUPID(),
//					new SequencedPause(.1), 
////					new DriveAtSetSpeedForADistance(-.7, -60),
//					new ResetDriveEncoders(),
//					new DriveStraight(-60),
//					new DisableDriveStraightPID(), 
//					//drop the totes
//					new InnerSequencer(createDropToteStackRoutine(false)), 
//					new DriveStraight(-80), //get away from the tote stack (Distance untested) 
//					new DisableDriveStraightPID(), 
			}); 
			//end of switch statement
		}
		return new Sequencer();
	}
	
	

	/*
	 * Updated lift routine as of 3-18-15..uses new MoveClawWithClapper SequencedItem
	 */
	public static Sequencer createToteLiftRoutine() {
		Sequencer s = new Sequencer(
				new SequencedItem[] {
						new ExtendRatchet(), 
						new RunRollers(0.6),
						new IncrementToteCount(),
						new RunRollers(0),
						new SetClapperPIDByToteCount(),
						new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
//						new RetractRatchet(), //Mr Collins said that this was not needed and made debugging harder
						new SequencedMultipleItem(
								new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
								new MoveClawWithClapper(MoveClawWithClapper.UP)
						), 
						new MoveClawConstantSpeed(0),
//						new ExtendRatchet(),
						new SequencedMultipleItem(
//								new RunRollers(.6), 
								new SequencedPause(.25)
						),
//						new RunRollers(0),
						new SetClapperPID(0.0005, 0, 0),
						new SequencedMultipleItem(
								new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT),
								new MoveClawWithClapper(MoveClawWithClapper.DOWN)
								),
						new MoveClawConstantSpeed(0),
						new SetClapperPIDByToteCount(),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT)
				}
			);
		
		return s;
	}
	
	
	/**
	 * This method lifts a tote up above the ratchet and then holds there. It is designed for use in our 3-tote auto.
	 * @return
	 */
	public static Sequencer createAutoToteLiftRoutinePartOne() {
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
//						new RunRollers(0.6),
						
//						new RunRollers(0),
						
//						new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
//						new SequencedMultipleItem(
						
//								new MoveClawWithClapper(MoveClawWithClapper.UP)
//						), 
//						new MoveClawConstantSpeed(0),
//						new ExtendRatchet(),
//						new SequencedMultipleItem(
//								new RunRollers(.6), 
//								new SequencedPause(.25)
//						),
//						new RunRollers(0),
//						new SetClapperPID(0.0005, 0, 0),
//						new SequencedMultipleItem(
//								new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT),
//								new MoveClawWithClapper(MoveClawWithClapper.DOWN)
//								),
//						new MoveClawConstantSpeed(0),
//						new SetClapperPIDByToteCount(),
//						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				}
				);
	}
	
	/**
	 * This method hangs a tote on the ratchet after already being lifted by the "PartOne" sequence. It
	 * is designed to use in our 3-tote auto. It also stops the rollers from moving.
	 * @return
	 */
	public static Sequencer createAutoToteLiftRoutinePartTwo() {
		return new Sequencer(
				new SequencedItem[] {
//						new ExtendRatchet(), 
//						new RunRollers(0.6),
//						new IncrementToteCount(),
//						new SetClapperPIDByToteCount(),
//						new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
//						new SequencedMultipleItem(
//								new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
//								new MoveClawWithClapper(MoveClawWithClapper.UP)
//						), 
//						new MoveClawConstantSpeed(0),
//						new ExtendRatchet(),
//						new SequencedMultipleItem(
//								new RunRollers(.6), 
//								new SequencedPause(.25)
//						),
//						new RunRollers(0),
						
//						new SequencedMultipleItem(
								
//								new MoveClawWithClapper(MoveClawWithClapper.DOWN)
//								),
//						new MoveClawConstantSpeed(0),
						new SequencedMultipleItem(
								new RunRollers(0),
								new SetClapperPID(0.0005, 0, 0),
								new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT, .25)
							),
						new SetClapperPIDByToteCount(),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT, 0.03)

				}
				);
	}
	
	/**
	 * @return sequencer that drops a tote stack and a container 
	 */
	public static Sequencer createDropToteStackRoutine(boolean toteBelowRatchet) { 
		// tune kp down a bit? add a seq
		
		Sequencer returnSequence = null;

		if (toteBelowRatchet) {
			
			double clawSetpoint = Claw.HIGHEST_POS - (Claw.POTS_PER_INCH * .5);
		
			switch (Robot.toteCounter.getCount()) {
				
				case 1: 
					clawSetpoint = Claw.DROP_SEQ_POS_2; // tote count is one less than number of totes that we have
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
			
			returnSequence = new Sequencer(
					new SequencedItem[] {
//							new IncrementToteCount(), //added 3/27
							new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
							new CloseClapper(),
							new RetractRatchet(),
							new SetClapperPIDByToteCount(),
							new SequencedMultipleItem(
									new MoveClapperVertically(
											Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT, 1.5),
									new MoveClawVertically(Claw.HIGHEST_POS - Claw.POTS_PER_INCH * .5
											- ((5 - Robot.toteCounter.getCount()) * (Claw.POTS_PER_INCH * 12)))
//									new MoveClawWithClapper(MoveClawWithClapper.UP)
									),
							new SequencedPause(.25), //change back to .5
							new SequencedMultipleItem(
									new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//									new MoveClawWithClapper(MoveClawWithClapper.DOWN)),
									new MoveClawVertically(clawSetpoint)
									),
							new OpenClaw(), 
							new OpenClapper(),
							new SequencedPause(.1), //change back to .1
							new MoveClawVertically(Robot.claw.getPotValue() + 15)//  Claw.HIGHEST_POS - (Claw.POTS_PER_INCH * 1.5))
						});
			
		} else {
			// assumption is that all of the totes are on the hook...start by
			// making sure that the clapper is in the correct position
			returnSequence = new Sequencer(new SequencedItem[] {
					new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
					new SequencedMultipleItem(
							new OpenClapper(),
							new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT)),
					new CloseClapper(),
					new SequencedMultipleItem(
							new MoveClapperVerticallyForToteDrop(Clapper.ABOVE_RATCHET_SETPOINT),
							new MoveClawWithClapper(MoveClawWithClapper.UP)),
					new RetractRatchet(),
					new OpenClaw(),
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.LOADING_SETPOINT), 
							new MoveClawWithClapper(MoveClawWithClapper.DOWN)), 
					new OpenClapper() });
		}
		
		Robot.toteCounter.resetCount();
		return returnSequence;
	}

	public static Sequencer createContainerRightingRoutine() {
		
		if (Robot.toteCounter.getCount() != 0)
			return null; 
		
		return new Sequencer(new SequencedItem[] {
//				new DriveStraight(-3), 
				new CloseClapper(), 
				new SetClapperPID(Clapper.kP_DEFAULT, 0, 0),
				new SequencedPause(.5), //probably not needed, but added this for testing
				new MoveClapperVertically(Clapper.RIGHTING_CONTAINER_POS), 
				new SequencedPause(.25), //this one is actually probably needed
				new OpenClapper()
		});
	}
	
	public static Sequencer createPrepareForContainerRightingRoutine() {
		
		if (Robot.toteCounter.getCount() != 0) {
			return null;
		}
		
		return new Sequencer(new SequencedItem[] {
				new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
				new SetClapperPID(Clapper.kP_DEFAULT, 0, 0),
				new SequencedMultipleItem(
						new MoveClawVertically(Claw.ONE_AND_TWO_TOTE_RESTING_POS),
						new OpenClapper(),
						new MoveClapperVertically(Clapper.RIGHTING_CONTAINER_PRE_POS)
					)

		});
	}

	public static Sequencer createContainerPickupRoutine() {

		if (Robot.toteCounter.getCount() != 0)
			return null; 

		return new Sequencer(new SequencedItem[] {
				new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
				new CloseClaw(),
				new SequencedPause(.1),
				new MoveClawVertically(Claw.ONE_AND_TWO_TOTE_RESTING_POS),//see comment
				new CloseClapper(),
		});
	}

	public static Sequencer createAdjustClawOnContainerRoutine() {
		if (Robot.toteCounter.getCount() != 2)
			return null; //return null instead of empty sequence so driver and op still have control
		return new Sequencer(new SequencedItem[] {
				new MoveClawVertically(Claw.CONTAINER_ADJUSTMANT_POS),
				new SequencedPause(.25), 
				new OpenClaw(), 
				new MoveClawVertically(Claw.TWO_TOTE_PLACEMENT_POS), 
				new CloseClaw(),
				new MoveClawVertically(Claw.TWO_TOTE_PLACEMENT_POS + Claw.POTS_PER_INCH * 1)
		}); 
	}
	
	public static Sequencer createPrepareForContainerLiftRoutine() {

		if (Robot.toteCounter.getCount() != 0)
			return null; 
		
		return new Sequencer(new SequencedItem[] { 
				new OpenClapper(), 
				new CloseClaw(),
				new RetractRatchet(), 
				new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),		
				new SequencedMultipleItem(						
						new MoveClawVertically(Claw.LOWEST_POS),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT)
				),
				new OpenClaw() });
	}

	
	
		/*
	private static Sequencer createDropToteStackRoutineKeepContainer(boolean withToteBelowRatchet) { // tune kp down a bit? add a seq
				// pause?
		
		Sequencer returnSequence = null;

		if (withToteBelowRatchet)
			returnSequence = new Sequencer(
					new SequencedItem[] {
							new SequencedMultipleItem(
									new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
									new CloseClapper(),
									new SetFingersPos(Fingers.OPEN)),
									new SequencedMultipleItem(
											new MoveClapperVertically(Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT),
											new MoveClawRelativeToClapper(Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT)),
									new RetractRatchet(),
									new SequencedPause(.1),
									new SequencedMultipleItem(
										new MoveClapperVertically(Clapper.LOADING_SETPOINT),
										new MoveClawRelativeToClapper(Clapper.LOADING_SETPOINT)),
									new OpenClapper() });
		else {
			// assumption is that all of the totes are on the hook...start by
			// making sure that the clapper is in the correct position
			returnSequence = new Sequencer(new SequencedItem[] {
					new SequencedMultipleItem(
							new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
							new OpenClapper(),
							new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT)),
							new SequencedMultipleItem(
								new CloseClapper(),
								new SetFingersPos(Fingers.OPEN)),
							new SequencedMultipleItem(
									new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
									new MoveClawRelativeToClapper(Clapper.ABOVE_RATCHET_SETPOINT)),
									new RetractRatchet(),
							new SequencedMultipleItem(
									new MoveClapperVertically(Clapper.LOADING_SETPOINT), 
									new MoveClawRelativeToClapper(Clapper.LOADING_SETPOINT)), 
									new OpenClapper() });
		}
		
		Robot.toteCounter.resetCount();
		return returnSequence;
		
	} */
	
//	private static Sequencer createTotePickupAndLowerClapperRoutine() {
//		return new Sequencer(new SequencedItem[] {
//				new InnerSequencer(createToteIntakeRoutine()), 
//				new MoveClapperVertically(Clapper.LOADING_SETPOINT)
//		}); 
//	}
	
	/*
	 * Used to work through the following logic...became the basis of our lifting sequence.
	 *
	public static Sequencer createTempClawFollowClapper() {
		return new Sequencer(
				new SequencedItem[] {
						new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
						new RetractRatchet(),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
						new IncrementToteCount(),
						new SetClapperPIDByToteCount(),
						new ExtendRatchet(),
						new SequencedPause(0.1),
						new SequencedMultipleItem(
								new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
								new MoveClawWithClapper(MoveClawWithClapper.UP)),
						new MoveClawConstantSpeed(0),
						new SequencedPause(0.1),
						new SetClapperPID(0.01, 0, 0),
						new SequencedMultipleItem(
								new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT),
								new MoveClawWithClapper(MoveClawWithClapper.DOWN)),
						new MoveClawConstantSpeed(0),
						new SetClapperPIDByToteCount(),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//						new SequencedPause(0.1) 
					}
				);
		} 
	*/
//	public static Sequencer createToteIntakeNoHang() {
//
//		return new Sequencer(new SequencedItem[] {
////				new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
//				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//				new SequencedMultipleItem(new CloseClapper(),
//						new SetFingersPos(Fingers.CLOSED),
//						new SetRollers(SetRollers.INTAKE, 2, 1)),
//				new SequencedMultipleItem(new SetRollers(
//						SetRollers.OFF, .1, 0), new CloseClapper(),
//						new SetFingersPos(Fingers.PARALLEL)
//						),
////				new MoveClapperVertically(Clapper.SCORING_PLATFORM_HEIGHT)
//		});
//	}

//	public static Sequencer createToteIntakeWithHang() {
//
//		double kP = Robot.clapper.getkP();
//		double kI = Robot.clapper.getkI();
//		double kD = Robot.clapper.getkD();
//
//		return new Sequencer(new SequencedItem[] {
//				new RetractRatchet(),
//				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//				new SequencedMultipleItem(new CloseClapper(),
//						new SetFingersPos(Fingers.CLOSED),
//						new SetFingerRollers(SetFingerRollers.INTAKE, 2, 1)),
//				new SequencedMultipleItem(new SetFingerRollers(
//						SetFingerRollers.OFF, .1, 0), new CloseClapper(),
//						new SetFingersPos(Fingers.PARALLEL)),
//				new IncrementToteCount(),
//				new SetClapperPIDByToteCount(),
//				new SequencedPause(0.25), // TODO: check this
//				new SequencedMultipleItem(new ExtendRatchet(),
//						new MoveClapperVertically(
//								Clapper.ABOVE_RATCHET_SETPOINT),
//						new MoveClawWithToteIntake(), new SetFingerRollers(
//								SetFingerRollers.INTAKE, .5, .5)),
//				new SequencedPause(0.25), // TODO: check this
//				new SetFingerRollers(SetFingerRollers.OFF, .05, 0),
//				new SequencedPause(0.1),
//				new SetClapperPID(0.001, 0, 0),
//				new SequencedMultipleItem(new MoveClapperVertically(
//						Clapper.HOLDING_TOTE_SETPOINT),
//						new MoveClawRelativeToClapper(
//								Clapper.HOLDING_TOTE_SETPOINT)),
//				new SetClapperPIDByToteCount() });
//	}
//	public static Sequencer createContainerLiftWithPositionFix() {
//	return new Sequencer(new SequencedItem[] {
//			new SetClawPID(Claw.AGGRESSIVE_KP, 0, 0),
//			new CloseClaw(),
//			// new MoveClawVertically(Claw.ONE_TOTE_LOADING), //see comment
//			// below
//
//			// added this to attempt fixing the positioning of the container
//			// within the claw...then put the container down in a position
//			// ready for the first tote to slide in (just above the bottom
//			// of the claw's lower plate)
//			new MoveClawVertically(Claw.FIX_CONTAINER_POSITION_IN_CLAW),
//			new CloseClapper(),
//			new MoveClapperVertically(Clapper.FIX_CONTAINER_IN_CLAW_POS),
//			new SequencedPause(.25),
//			new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//			new SetClawPID(Claw.kP, Claw.kI, Claw.kD),
//			new SequencedMultipleItem(
//					new SetFingersPos(Fingers.OPEN),
//					new MoveClawVertically(Claw.FIRST_TOTE_POSITION_BELOW_RATCHET)) });
//}	

//	/**
//	 * @return Sequence that intakes a tote using the strongback tilt method
//	 * This is the intake routine used at Inland Empire; use the new lifting routine instead.
//	 * @deprecated
//	 */
////	public static Sequencer createToteIntakeRoutine() {
//		return new Sequencer(
//				new SequencedItem[] {
//						new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
//						new RetractRatchet(),
//						new TiltStrongback(0), 
//						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//						new SequencedMultipleItem(new CloseClapper(),
//								new SetFingersPos(Fingers.CLOSED),
//								new SetRollers(SetRollers.INTAKE,
//										2, 1)),
//						new SequencedMultipleItem(new SetRollers(
//								SetRollers.INTAKE, .25, .5),
//								new CloseClapper(), new SetFingersPos(
//										Fingers.PARALLEL)),
//
//						new IncrementToteCount(),
//						new SetClapperPIDByToteCount(),
//						new SequencedPause(0.25), // TODO: check this
//						new TiltStrongback(7), 
//						new SequencedPause(0.5),
//						new SequencedMultipleItem(
//								new MoveClapperVertically(
//										Clapper.ABOVE_RATCHET_SETPOINT),
////								new MoveClawWithToteIntake(),
////								new TiltStrongback(7), 
//								new SetRollers(
//										SetRollers.INTAKE, .25, .5)),
//						// new SequencedPause(0.25), // TODO: check this
//						new SetRollers(SetRollers.OFF, .1, 0),
//						new ExtendRatchet(),
//						new SequencedPause(0.1),
//						new SetClapperPID(0.001, 0, 0),
//						new SequencedMultipleItem(new MoveClapperVertically(
//								Clapper.HOLDING_TOTE_SETPOINT),
//								new MoveClawRelativeToClapper(
//										Clapper.HOLDING_TOTE_SETPOINT)),
//						new SetClapperPIDByToteCount(), 
//						new TiltStrongback(0),
////						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//						new SequencedPause(0.5), //TODO: check pause duration to see if strongback rights itself
//						new SetFingersPos(Fingers.OPEN),
//						new DisableStrongbackPID() });
//	}

}
