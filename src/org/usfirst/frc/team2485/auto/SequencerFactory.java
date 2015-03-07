package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.auto.SequencedItems.AutoTestPrint;
import org.usfirst.frc.team2485.auto.SequencedItems.ClampOutputRangeDriveStraightPID;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableClawPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableStrongbackPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveAtSetSpeed;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraight;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraightLowAcceleration;
import org.usfirst.frc.team2485.auto.SequencedItems.ExtendRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.IncrementToteCount;
import org.usfirst.frc.team2485.auto.SequencedItems.CommandeerContainerSequence;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClapperVertically;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawRelativeToClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawVertically;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClawWithToteIntake;
import org.usfirst.frc.team2485.auto.SequencedItems.OpenClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.OpenClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.RetractRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.RotateToAngle;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClapperPID;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClapperPIDByToteCount;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClawPID;
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingerRollers;
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingersPos;
import org.usfirst.frc.team2485.auto.SequencedItems.TiltStrongback;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Clapper;
import org.usfirst.frc.team2485.subsystems.Claw;
import org.usfirst.frc.team2485.subsystems.ContainerCommandeerer;
import org.usfirst.frc.team2485.subsystems.Fingers;

public class SequencerFactory {

	// auto types
	public static final int SEQ_TEST = -1, 
			DRIVE_TO_AUTO_ZONE = 0,
			ONE_TOTE_ONE_CONTAINER = 1, 
			TWO_TOTE_ONE_CONTAINER = 2, 
			THREE_TOTE_STRAIGHT = 3,
			THREE_TOTE_PUSH_CONTAINERS = 4, 
			CONTAINER_AND_TOTE = 5,
			CONTAINER_STEAL = 6,
			SECRET_CONTAINER_STEAL_START_LEFT = 7,
			SECRET_CONTAINER_STEAL_START_RIGHT = 8, 
			ONE_CONTAINER = 9;

	public static Sequencer createAuto(int autoType) {

		switch (autoType) {
		case SEQ_TEST:
			return new Sequencer(new SequencedItem[] { new AutoTestPrint(), });
		case DRIVE_TO_AUTO_ZONE:
			return new Sequencer(new SequencedItem[] {
					new DriveStraight(70), // TODO:
//					new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),											// fix															// this
					});
		case ONE_TOTE_ONE_CONTAINER:
			return new Sequencer(
					new SequencedItem[] {
							new SequencedMultipleItem(new CloseClapper(),
//									new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
									new SetFingersPos(Fingers.PARALLEL),
									new SetFingerRollers(
											SetFingerRollers.INTAKE, 1, 1)),
							new SequencedPause(0.7),
							// new SequencedMultipleItem(
							// new SetFingersPos(Fingers.PARALLEL),
							new MoveClapperVertically(
									Clapper.ABOVE_RATCHET_SETPOINT),
							new SetFingerRollers(SetFingerRollers.OFF, .1, 0),
							// ),
							new SequencedPause(0.3),
							new MoveClapperVertically(Clapper.LOADING_SETPOINT),
							new RotateToAngle(90), // TODO: fix this
							new DriveStraight(60) // TODO: fix this
					});

		case TWO_TOTE_ONE_CONTAINER:
			return new Sequencer(
					new SequencedItem[] {
							new InnerSequencer(createContainerPickupRoutine()), 
							new DriveStraight(15), // TODO: untested distance
							new InnerSequencer(createToteIntakeForAutonomous()), 
							new DriveStraight(33), // TODO: untested distance
							new RotateToAngle(15), //push container out of the way
							new RotateToAngle(0), 
							new DriveStraight(5), // TODO: untested distance
							new InnerSequencer(createToteIntakeForAutonomous()), 
							new RotateToAngle(90), // TODO: untested distance
							new DriveStraight(15, 0), // TODO: untested distance
							new InnerSequencer(createDropToteStackRoutine(false))
					});

//		case ONE_CONTAINER_THREE_TOTES: //assumes that teammates push the other containers out of our way
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

		case THREE_TOTE_PUSH_CONTAINERS:
			return new Sequencer(
					new SequencedItem[] { 
							
							});
			
		/*
		 * Starting off behind the container
		 */
		case CONTAINER_AND_TOTE:
			return new Sequencer(
					new SequencedItem[] {
							// first pick up container
							
							new InnerSequencer(createContainerPickupRoutine()),
							new OpenClapper(),
							new DriveStraight(10),
							new InnerSequencer(createTestPickupWithStrongbackTilt()),
							new RotateToAngle(90),
							new DriveStraight(60), 

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
			

			// case SECRET_CONTAINER_STEAL_START_LEFT:
			// return new Sequencer(new SequencedItem[] {
			// new DriveStraight(60)
			// });
			//
			// case SECRET_CONTAINER_STEAL_START_RIGHT:
			// return new Sequencer(new SequencedItem[] {
			// new DriveStraight(60)
			// });
			
		case ONE_CONTAINER:
			return new Sequencer(new SequencedItem[] {
					new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
					new CloseClaw(),
					new MoveClawVertically(Claw.ONE_TOTE_RESTING),
					new DriveStraight(-60) //TODO: check this
			});

		}
		

			
		return new Sequencer();
	}

	public static Sequencer createToteIntakeNoHang() {

		return new Sequencer(new SequencedItem[] {
//				new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SequencedMultipleItem(new CloseClapper(),
						new SetFingersPos(Fingers.CLOSED),
						new SetFingerRollers(SetFingerRollers.INTAKE, 2, 1)),
				new SequencedMultipleItem(new SetFingerRollers(
						SetFingerRollers.OFF, .1, 0), new CloseClapper(),
						new SetFingersPos(Fingers.PARALLEL)
						),
//				new MoveClapperVertically(Clapper.SCORING_PLATFORM_HEIGHT)
		});
	}

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

	public static Sequencer createDropToteStackRoutine(boolean withToteBelowRatchet) { 
		// tune kp down a bit? add a seq
		
		Sequencer returnSequence = null;

		if (withToteBelowRatchet)
			returnSequence = new Sequencer(
					new SequencedItem[] {
							new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
							new SequencedMultipleItem(
									new CloseClapper(),
									new SetFingersPos(Fingers.OPEN)),
							new SequencedMultipleItem(
									new MoveClapperVertically(
											Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT),
									new MoveClawRelativeToClapper(
											Clapper.LIFT_BOTTOM_TOTE_TO_RAISE_STACK_OFF_RATCHET_SETPOINT)),
							new RetractRatchet(),
							new SequencedPause(.1),
							new SequencedMultipleItem(
									new MoveClapperVertically(Clapper.LOADING_SETPOINT),
									new MoveClawRelativeToClapper(Clapper.LOADING_SETPOINT)),
							new OpenClaw(), 
							new OpenClapper() });
		else {
			// assumption is that all of the totes are on the hook...start by
			// making sure that the clapper is in the correct position
			returnSequence = new Sequencer(new SequencedItem[] {
					new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
					new SequencedMultipleItem(
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
					new OpenClaw(),
					new OpenClapper() });
		}
		
		Robot.toteCounter.resetCount();
		return returnSequence;
	}

	public static Sequencer createContainerRightingRoutine() {
		double kP = Robot.clapper.getkP();
		double kI = Robot.clapper.getkI();
		double kD = Robot.clapper.getkD();
		return new Sequencer(new SequencedItem[] {
//				new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
				new SetFingersPos(Fingers.CLOSED), new SequencedPause(0.4),
				new SetClapperPID(0.0075, kI, kD),
				new MoveClapperVertically(Clapper.COOP_TWO_TOTES_SETPOINT), 
				new SequencedPause(0.4),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SetFingersPos(Fingers.PARALLEL),
				new SetClapperPID(kP, kI, kD)

		});
	}

	public static Sequencer createContainerPickupRoutine() {
		return new Sequencer(new SequencedItem[] {
				new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
				new CloseClaw(),
				new MoveClawVertically(Claw.ONE_TOTE_LOADING), //see comment
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
		return new Sequencer(new SequencedItem[] { 
				new OpenClapper(), 
				new CloseClaw(),
				new RetractRatchet(), 
				new SequencedMultipleItem(
						new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
						new SetFingersPos(Fingers.OPEN), 
						new MoveClawVertically(Claw.CONTAINER_LOADING_POINT),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT)
				),
				new OpenClaw() });
	}

	public static Sequencer createTestPickupWithStrongbackTilt() {

		return new Sequencer(
				new SequencedItem[] {
						new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
						new RetractRatchet(),
						new TiltStrongback(0), 
						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
						new SequencedMultipleItem(new CloseClapper(),
								new SetFingersPos(Fingers.CLOSED),
								new SetFingerRollers(SetFingerRollers.INTAKE,
										2, 1)),
						new SequencedMultipleItem(new SetFingerRollers(
								SetFingerRollers.INTAKE, .25, .5),
								new CloseClapper(), new SetFingersPos(
										Fingers.PARALLEL)),

						new IncrementToteCount(),
						new SetClapperPIDByToteCount(),
						new SequencedPause(0.25), // TODO: check this
						new TiltStrongback(7), 
						new SequencedPause(0.5),
						new SequencedMultipleItem(
								new MoveClapperVertically(
										Clapper.ABOVE_RATCHET_SETPOINT),
								new MoveClawWithToteIntake(),
//								new TiltStrongback(7), 
								new SetFingerRollers(
										SetFingerRollers.INTAKE, .25, .5)),
						// new SequencedPause(0.25), // TODO: check this
						new SetFingerRollers(SetFingerRollers.OFF, .1, 0),
						new ExtendRatchet(),
						new SequencedPause(0.1),
						new SetClapperPID(0.001, 0, 0),
						new SequencedMultipleItem(new MoveClapperVertically(
								Clapper.HOLDING_TOTE_SETPOINT),
								new MoveClawRelativeToClapper(
										Clapper.HOLDING_TOTE_SETPOINT)),
						new SetClapperPIDByToteCount(), 
						new TiltStrongback(0),
//						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
						new SequencedPause(0.5), //TODO: check pause duration to see if strongback rights itself
						new SetFingersPos(Fingers.OPEN),
						new DisableStrongbackPID() });
	}
	
	/*
	 * Returns a Sequencer that drives forward and intakes one tote
	 */
	private static Sequencer createToteIntakeForAutonomous() {
		return new Sequencer (
				new SequencedItem[] {
						new SequencedMultipleItem(
//								new SetClawPID(Claw.kP_LESS_POWER_ALLOWS_MORE_ERROR, Robot.claw.getI(), Robot.claw.getD()),
								new DriveStraight(81),
								new MoveClapperVertically(Clapper.LOADING_SETPOINT)), 
						new SetFingerRollers(SetFingerRollers.LEFT, 1, 1),
						new DriveStraight(12),
						new SequencedMultipleItem(
								new SetFingersPos(Fingers.PARALLEL),
								new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1)),
		// we should now have one tote on the ratchet and another up against the strongback but still on the floor
						new SequencedPause(.2),
						new SetFingersPos(Fingers.PARALLEL),
						new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
						new SequencedPause(.2) });
	}
	
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
		
	}
	private static Sequencer createTestPickupWithStrongbackTiltAndLowerClapper() {
		return new Sequencer(new SequencedItem[] {
				new InnerSequencer(createTestPickupWithStrongbackTilt()), 
				new MoveClapperVertically(Clapper.LOADING_SETPOINT)
		}); 
	}
}
