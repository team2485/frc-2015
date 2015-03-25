package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.auto.SequencedItems.CloseClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.CommandeerContainerSequence;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableStrongbackPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraight;
import org.usfirst.frc.team2485.auto.SequencedItems.ExtendRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.IncrementToteCount;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClapperVertically;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawConstantSpeed;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawVertically;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawWithClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.OpenClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.OpenClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.RetractRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.RotateToAngle;
import org.usfirst.frc.team2485.auto.SequencedItems.RunRollers;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClapperPID;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClapperPIDByToteCount;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClawPID;
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingersPos;
import org.usfirst.frc.team2485.auto.SequencedItems.SetRollers;
import org.usfirst.frc.team2485.auto.SequencedItems.TiltStrongback;
import org.usfirst.frc.team2485.auto.SequencedItems.ToteIntake;
import org.usfirst.frc.team2485.auto.SequencedItems.ZeroEncoders;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Clapper;
import org.usfirst.frc.team2485.subsystems.Claw;
import org.usfirst.frc.team2485.subsystems.Fingers;

public class SequencerFactory {

	// auto types
	public static final int  
			DRIVE_TO_AUTO_ZONE = 0, 
			ONE_CONTAINER = 1,
			CONTAINER_STEAL = 2,
			THREE_TOTE = 3; 
			
	public static Sequencer createAuto(int autoType) {

		switch (autoType) {
		
		case DRIVE_TO_AUTO_ZONE:
			return new Sequencer(new DriveStraight(70));

		case ONE_CONTAINER:
			return new Sequencer(new SequencedItem[] {
					new OpenClapper(), 
					new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
					new CloseClaw(),
					new MoveClawVertically(Claw.ONE_AND_TWO_TOTE_RESTING_POS),
					new MoveClawConstantSpeed(0), 
					new RotateToAngle(-90),
					new ZeroEncoders(),
					new DriveStraight(60)
			});
			
		case CONTAINER_STEAL:
			return new Sequencer(new SequencedItem[] {
//					new TiltStrongback(0),
//					new SequencedPause(1),
					new CommandeerContainerSequence(CommandeerContainerSequence.BOTH),
					new SequencedPause(1), //TODO: how long does the pause need to be?
					new DriveStraight(60),
					new TiltStrongback(0),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new SequencedPause(2),
					new DisableStrongbackPID(),
//					new RotateToAngle(angle)
			});
//		case THREE_TOTE: //assumes that teammates push the other containers out of our way
//			return new Sequencer(new SequencedItem[] {
//					new InnerSequencer(createContainerPickupRoutine()), 
////					new CloseClapper(),
////					new SetFingersPos(Fingers.CLOSED),
////					new SetFingerRollers(SetFingerRollers.LEFT, 1, 1),
//					new SequencedMultipleItem(
//							new InnerSequencer(createTestPickupWithStrongbackTiltAndLowerClapper()),
//							new ClampOutputRangeDriveStraightPID(-.5, .5), // TODO: find values
//							new DriveStraight(50)), //unknown distance
//					new SequencedMultipleItem(
//							new InnerSequencer(createTestPickupWithStrongbackTiltAndLowerClapper()),
//							new DriveStraight(100)), //unknown distance
//					new InnerSequencer(createTestPickupWithStrongbackTilt()), 
//					new RotateToAngle(90), // find angle
//					new DriveAtSetSpeed(-.6, 2), //tune 
//					new SequencedMultipleItem(
//							new InnerSequencer(createDropToteStackRoutineKeepContainer(false)),
//							new DriveAtSetSpeed(-.4, 1)
//							), 
//					new DriveAtSetSpeed(0, .1)
//					});	
		}
		return new Sequencer();
	}


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

	/**
	 * @return sequencer that drops a tote stack and a container 
	 */
	public static Sequencer createDropToteStackRoutine(boolean toteBelowRatchet) { 
		// tune kp down a bit? add a seq
		
		Sequencer returnSequence = null;

		if (toteBelowRatchet)
			returnSequence = new Sequencer(
					new SequencedItem[] {
							new SetClawPID(Claw.kP_LOCK_POSITION_IN_PLACE, Robot.claw.getI(), Robot.claw.getD()),
							new CloseClapper(),
							new RetractRatchet(),
							new SetClapperPIDByToteCount(),
							new SequencedMultipleItem(
									new MoveClapperVertically(
											Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT),
									new MoveClawVertically(Claw.HIGHEST_POS - Claw.POTS_PER_INCH * .5)
									),
							new SequencedPause(.5),
							new SequencedMultipleItem(
									new MoveClapperVertically(Clapper.LOADING_SETPOINT),
									new MoveClawWithClapper(MoveClawWithClapper.DOWN)),
							new OpenClaw(), 
							new OpenClapper(), 
							new SequencedPause(.1),
							new MoveClawVertically(Claw.HIGHEST_POS - Claw.POTS_PER_INCH*.5)
						});
		else {
			// assumption is that all of the totes are on the hook...start by
			// making sure that the clapper is in the correct position
			returnSequence = new Sequencer(new SequencedItem[] {
					new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
					new SequencedMultipleItem(
							new OpenClapper(),
							new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT)),
					new CloseClapper(),
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
							new MoveClawWithClapper(MoveClawWithClapper.UP)),
					new RetractRatchet(),
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.LOADING_SETPOINT), 
							new MoveClawWithClapper(MoveClawWithClapper.DOWN)), 
					new OpenClaw(),
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

//	public static Sequencer createContainerLiftWithPositionFix() {
//		return new Sequencer(new SequencedItem[] {
//				new SetClawPID(Claw.AGGRESSIVE_KP, 0, 0),
//				new CloseClaw(),
//				// new MoveClawVertically(Claw.ONE_TOTE_LOADING), //see comment
//				// below
//
//				// added this to attempt fixing the positioning of the container
//				// within the claw...then put the container down in a position
//				// ready for the first tote to slide in (just above the bottom
//				// of the claw's lower plate)
//				new MoveClawVertically(Claw.FIX_CONTAINER_POSITION_IN_CLAW),
//				new CloseClapper(),
//				new MoveClapperVertically(Clapper.FIX_CONTAINER_IN_CLAW_POS),
//				new SequencedPause(.25),
//				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
//				new SetClawPID(Claw.kP, Claw.kI, Claw.kD),
//				new SequencedMultipleItem(
//						new SetFingersPos(Fingers.OPEN),
//						new MoveClawVertically(Claw.FIRST_TOTE_POSITION_BELOW_RATCHET)) });
//	}	
	
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
	
	
	/*
	 * Updated lift routine as of 3-18-15..uses new MoveClawWithClapper SequencedItem
	 */
	public static Sequencer createToteLiftRoutine() {
		return new Sequencer(
				new SequencedItem[] {
						new ExtendRatchet(), 
						new IncrementToteCount(),
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
						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				}
				);
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
	public static Sequencer createAdjustClawOnContainerRoutine() {
		if (Robot.toteCounter.getCount() != 2)
			return null; //return null instead of empty sequence so driver and op still have control
		return new Sequencer(new SequencedItem[] {
				new MoveClawVertically(Claw.CONTAINER_ADJUSTMANT_POS),
				new SequencedPause(1), 
				new OpenClaw(), 
				new MoveClawVertically(Claw.TWO_TOTE_PLACEMENT_POS), 
				new CloseClaw()
		}); 
	}
}
