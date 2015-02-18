package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.auto.SequencedItems.AutoTestPrint;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableClawPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableStrongbackPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveSlowConstant;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraight;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraightLowAcceleration;
import org.usfirst.frc.team2485.auto.SequencedItems.ExtendRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.IncrementToteCount;
import org.usfirst.frc.team2485.auto.SequencedItems.LiberateContainer;
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
import org.usfirst.frc.team2485.subsystems.ContainerLiberator;
import org.usfirst.frc.team2485.subsystems.Fingers;

public class SequencerFactory {

	// auto types
	public static final int SEQ_TEST = -1, 
			DRIVE_TO_AUTO_ZONE = 0,
			ONE_TOTE = 1, 
			TWO_TOTE = 2, 
			THREE_TOTE_STRAIGHT = 3,
			THREE_TOTE_PUSH_CONTAINERS = 4, 
			CONTAINER_AND_TOTE = 5,
			CONTAINER_STEAL = 6,
			SECRET_CONTAINER_STEAL_START_LEFT = 7,
			SECRET_CONTAINER_STEAL_START_RIGHT = 8,
			MOBILITY = 9;

	public static Sequencer createAuto(int autoType) {

		switch (autoType) {
		case SEQ_TEST:
			return new Sequencer(new SequencedItem[] { new AutoTestPrint(), });
		case DRIVE_TO_AUTO_ZONE:
			return new Sequencer(new SequencedItem[] { new DriveStraight(60) // TODO:
																				// fix
																				// this
					});

		case ONE_TOTE:
			return new Sequencer(
					new SequencedItem[] {
							new SequencedMultipleItem(new CloseClapper(),
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

		case TWO_TOTE:
			return new Sequencer(new SequencedItem[] { new DriveStraight(60) // TODO:
																				// fix
																				// this
					});

		case THREE_TOTE_STRAIGHT:
			return new Sequencer(new SequencedItem[] {
					new CloseClapper(),
					new SequencedMultipleItem(new DriveSlowConstant(),
							new MoveClapperVertically(
									Clapper.ABOVE_RATCHET_SETPOINT)),
					new SetFingersPos(Fingers.PARALLEL),
					new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1),
					new SequencedPause(0.2),
					// new SequencedMultipleItem(

					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new DriveStraight(81),
					// new OpenClapper()
					// ),
					new SequencedMultipleItem(new OpenClapper(),
							new SetFingersPos(Fingers.CLOSED),
							new RetractRatchet(), new SetFingerRollers(
									SetFingerRollers.INTAKE, 1, 1),
							new DriveSlowConstant()),
					new SequencedPause(.2),
					new SequencedMultipleItem(
							// new SetFingerRollers(SetFingerRollers.REVERSE,
							// 1),
							new SetFingersPos(Fingers.PARALLEL),
							new ExtendRatchet(), new DriveSlowConstant()),
					new CloseClapper(),
					new SequencedPause(.2),
					// new SetFingerRollers(SetFingerRollers.INTAKE, 1),
					// new SequencedMultipleItem(
					// new SetFingersPos(Fingers.PARALLEL),
					// new SequencedPause(1),
					new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
					new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1), // 0.2
					new SequencedPause(.2),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new DriveStraightLowAcceleration(81),
					new SequencedMultipleItem(new OpenClapper(),
							new SetFingersPos(Fingers.CLOSED),
							new RetractRatchet(), new SetFingerRollers(
									SetFingerRollers.INTAKE, 1, 1),
							new DriveSlowConstant()),
					new SequencedPause(.2),
					new SequencedMultipleItem(
							// new SetFingerRollers(SetFingerRollers.REVERSE,
							// 1),
							new SetFingersPos(Fingers.PARALLEL),
							new ExtendRatchet(), new DriveSlowConstant()),
					new CloseClapper(),
					new SequencedPause(.2),
					// new SetFingerRollers(SetFingerRollers.INTAKE, 1),
					// new SequencedMultipleItem(
					// new SetFingersPos(Fingers.PARALLEL),
					// new SequencedPause(1),
					new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
					new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1), // 0.2
					new SequencedPause(.2),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT) });

		case THREE_TOTE_PUSH_CONTAINERS:
			return new Sequencer(new SequencedItem[] { new DriveStraight(81),
					new SequencedPause(1), new DriveStraight(81) });

		/*
		 * Starting off behind the container
		 */
		case CONTAINER_AND_TOTE:
			return new Sequencer(
					new SequencedItem[] {
							// first pick up tote
							new SequencedMultipleItem(
									new SetFingersPos(Fingers.CLOSED), 
									new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1)),
							new SequencedMultipleItem(new SetFingersPos(
									Fingers.PARALLEL),
									new MoveClapperVertically(
											Clapper.ABOVE_RATCHET_SETPOINT),
									new SetFingerRollers(SetFingerRollers.OFF,
											.1, 0)), new DriveStraight(20), // or
																			// whatever
																			// that
																			// is
					// then pick up container
					// no idea how to do that
					// then finish the auto and stuff

					});

		case CONTAINER_STEAL:
			return new Sequencer(new SequencedItem[] {
					new LiberateContainer(LiberateContainer.BOTH),
					new DriveStraight(60), });
			
		case MOBILITY:
			return new Sequencer(new SequencedItem[] {
					new DriveStraight(60), });

			// case SECRET_CONTAINER_STEAL_START_LEFT:
			// return new Sequencer(new SequencedItem[] {
			// new DriveStraight(60)
			// });
			//
			// case SECRET_CONTAINER_STEAL_START_RIGHT:
			// return new Sequencer(new SequencedItem[] {
			// new DriveStraight(60)
			// });

		}
		return new Sequencer();
	}

	public static Sequencer createToteIntakeNoHang() {

		return new Sequencer(new SequencedItem[] {
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SequencedMultipleItem(new CloseClapper(),
						new SetFingersPos(Fingers.CLOSED),
						new SetFingerRollers(SetFingerRollers.INTAKE, 2, 1)),
				new SequencedMultipleItem(new SetFingerRollers(
						SetFingerRollers.OFF, .1, 0), new CloseClapper(),
						new SetFingersPos(Fingers.PARALLEL)
						),
				new MoveClapperVertically(Clapper.SCORING_PLATFORM_HEIGHT)
		});
	}

	public static Sequencer createToteIntakeWithHang() {

		double kP = Robot.clapper.getkP();
		double kI = Robot.clapper.getkI();
		double kD = Robot.clapper.getkD();

		return new Sequencer(new SequencedItem[] {
				new RetractRatchet(),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SequencedMultipleItem(new CloseClapper(),
						new SetFingersPos(Fingers.CLOSED),
						new SetFingerRollers(SetFingerRollers.INTAKE, 2, 1)),
				new SequencedMultipleItem(new SetFingerRollers(
						SetFingerRollers.OFF, .1, 0), new CloseClapper(),
						new SetFingersPos(Fingers.PARALLEL)),
				new IncrementToteCount(),
				new SetClapperPIDByToteCount(),
				new SequencedPause(0.25), // TODO: check this
				new SequencedMultipleItem(new ExtendRatchet(),
						new MoveClapperVertically(
								Clapper.ABOVE_RATCHET_SETPOINT),
						new MoveClawWithToteIntake(), new SetFingerRollers(
								SetFingerRollers.INTAKE, .5, .5)),
				new SequencedPause(0.25), // TODO: check this
				new SetFingerRollers(SetFingerRollers.OFF, .05, 0),
				new SequencedPause(0.1),
				new SetClapperPID(0.001, 0, 0),
				new SequencedMultipleItem(new MoveClapperVertically(
						Clapper.HOLDING_TOTE_SETPOINT),
						new MoveClawRelativeToClapper(
								Clapper.HOLDING_TOTE_SETPOINT)),
				new SetClapperPIDByToteCount() });
	}

	public static Sequencer createDropToteStackRoutine(
			boolean withToteBelowRatchet) { // tune kp down a bit? add a seq
											// pause?

		Sequencer returnSequence = null;

		if (withToteBelowRatchet)
			returnSequence = new Sequencer(
					new SequencedItem[] {
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
									new MoveClapperVertically(
											Clapper.LOADING_SETPOINT),
									new MoveClawRelativeToClapper(
											Clapper.LOADING_SETPOINT)),
							new OpenClaw(), 
							new OpenClapper() });
		else {
			// assumption is that all of the totes are on the hook...start by
			// making sure that the clapper is in the correct position
			returnSequence = new Sequencer(new SequencedItem[] {
					new SequencedMultipleItem(
							new OpenClapper(),
							new MoveClapperVertically(
									Clapper.HOLDING_TOTE_SETPOINT)),
					new SequencedMultipleItem(
							new CloseClapper(),
							new SetFingersPos(Fingers.OPEN)),
					new SequencedMultipleItem(new MoveClapperVertically(
							Clapper.ABOVE_RATCHET_SETPOINT),
							new MoveClawRelativeToClapper(
									Clapper.ABOVE_RATCHET_SETPOINT)),
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
				new SetFingersPos(Fingers.CLOSED), new SequencedPause(0.4),
				new SetClapperPID(0.0075, kI, kD),
				new MoveClapperVertically(260), new SequencedPause(0.4),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SetFingersPos(Fingers.PARALLEL),
				new SetClapperPID(kP, kI, kD)

		});
	}

	public static Sequencer createContainerPickupRoutine() {
		return new Sequencer(new SequencedItem[] {
				new CloseClaw(),
				new MoveClawVertically(Claw.ONE_TOTE_LOADING), //see comment
		});
	}

	public static Sequencer createContainerLiftWithPositionFix() {
		return new Sequencer(new SequencedItem[] {
				new SetClawPID(Claw.AGGRESSIVE_KP, 0, 0),
				new CloseClaw(),
				// new MoveClawVertically(Claw.ONE_TOTE_LOADING), //see comment
				// below

				// added this to attempt fixing the positioning of the container
				// within the claw...then put the container down in a position
				// ready for the first tote to slide in (just above the bottom
				// of the claw's lower plate)
				new MoveClawVertically(Claw.FIX_CONTAINER_POSITION_IN_CLAW),
				new CloseClapper(),
				new MoveClapperVertically(Clapper.FIX_CONTAINER_IN_CLAW_POS),
				new SequencedPause(.25),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SetClawPID(Claw.kP, Claw.kI, Claw.kD),
				new SequencedMultipleItem(new SetFingersPos(Fingers.OPEN),
						new MoveClawVertically(
								Claw.FIRST_TOTE_POSITION_BELOW_RATCHET)) });
	}	
	
	public static Sequencer createPrepareForContainerLiftRoutine() {
		return new Sequencer(new SequencedItem[] { new SequencedMultipleItem(
				new OpenClapper(), new RetractRatchet(), new SetFingersPos(
						Fingers.OPEN), new OpenClaw(), new MoveClawVertically(
						Claw.CONTAINER_LOADING_POINT),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT)) });
	}

	public static Sequencer createTestPickupWithStrongbackTilt() {

		return new Sequencer(
				new SequencedItem[] {
						new RetractRatchet(),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
						new SequencedMultipleItem(new CloseClapper(),
								new SetFingersPos(Fingers.CLOSED),
								new SetFingerRollers(SetFingerRollers.INTAKE,
										2, 1)),
						new SequencedMultipleItem(new SetFingerRollers(
								SetFingerRollers.OFF, .1, 0),
								new CloseClapper(), new SetFingersPos(
										Fingers.PARALLEL)),

						new IncrementToteCount(),
						new SetClapperPIDByToteCount(),
						// new SequencedPause(0.25), // TODO: check this
						new SequencedMultipleItem(new ExtendRatchet(),
								new MoveClapperVertically(
										Clapper.ABOVE_RATCHET_SETPOINT),
								new MoveClawWithToteIntake(),
								new TiltStrongback(6), 
								new SetFingerRollers(
										SetFingerRollers.INTAKE, .25, .5)),
						// new SequencedPause(0.25), // TODO: check this
						new SetFingerRollers(SetFingerRollers.OFF, .1, 0),
						new SequencedPause(0.1),
						new SetClapperPID(0.001, 0, 0),
						new SequencedMultipleItem(new MoveClapperVertically(
								Clapper.HOLDING_TOTE_SETPOINT),
								new MoveClawRelativeToClapper(
										Clapper.HOLDING_TOTE_SETPOINT)),
						new SetClapperPIDByToteCount(), 
						new TiltStrongback(0),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT),
						new SetFingersPos(Fingers.OPEN),
						new DisableStrongbackPID() });
	}
}
