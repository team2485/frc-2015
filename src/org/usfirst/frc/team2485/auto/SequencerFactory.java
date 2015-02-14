package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.auto.SequencedItems.AutoTestPrint;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveSlowConstant;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraight;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraightLowAcceleration;
import org.usfirst.frc.team2485.auto.SequencedItems.MoveClapperVertically;
import org.usfirst.frc.team2485.auto.SequencedItems.OpenClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.RetractRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.ResetRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.RotateToAngle;
import org.usfirst.frc.team2485.auto.SequencedItems.SetClapperPID;
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingerRollers;
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingersPos;
import org.usfirst.frc.team2485.auto.SequencedItems.ToteIntake;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Clapper;
import org.usfirst.frc.team2485.subsystems.Fingers;


public class SequencerFactory {

	//auto types
	public static final int 
	SEQ_TEST 							= -1,
	DRIVE_TO_AUTO_ZONE 					= 0,
	ONE_TOTE 							= 1,
	TWO_TOTE 							= 2,
	THREE_TOTE_STRAIGHT					= 3, 
	THREE_TOTE_PUSH_CONTAINERS 			= 4,
	CONTAINER_AND_TOTE					= 5,
	CONTAINER_STEAL_LEFT				= 6, 
	CONTAINER_STEAL_RIGHT 				= 7, 
	SECRET_CONTAINER_STEAL_START_LEFT 	= 8,
	SECRET_CONTAINER_STEAL_START_RIGHT 	= 9;

	public static Sequencer createAuto(int autoType) {

		switch (autoType) {
		case SEQ_TEST: 
			return new Sequencer(new SequencedItem[] {
					new AutoTestPrint(), 	
			}); 
		case DRIVE_TO_AUTO_ZONE: 
			return new Sequencer(new SequencedItem[] {
					new DriveStraight(60) //TODO: fix this
			});

		case ONE_TOTE: 
			return new Sequencer(new SequencedItem[] {
					new SequencedMultipleItem(
							new CloseClapper(),
							new SetFingersPos(Fingers.PARALLEL), 
							new SetFingerRollers(SetFingerRollers.INTAKE, 1)
							), 
					new SequencedPause(0.7),
//					new SequencedMultipleItem(
//					new SetFingersPos(Fingers.PARALLEL),
					new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
					new SetFingerRollers(SetFingerRollers.OFF, .1),
//									),
					new SequencedPause(0.3),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new RotateToAngle(90), //TODO: fix this
					new DriveStraight(60) //TODO: fix this
			}); 

		case TWO_TOTE: 
			return new Sequencer(new SequencedItem[] {
					new DriveStraight(60) //TODO: fix this
			}); 

		case THREE_TOTE_STRAIGHT: 
			return new Sequencer(new SequencedItem[] {
					new SequencedMultipleItem(
							new CloseClapper(),
							new SetFingersPos(Fingers.PARALLEL), 
							new RetractRatchet(),
							new SetFingerRollers(SetFingerRollers.INTAKE, 1)
							), 
					new SequencedMultipleItem(
							new SequencedPause(0.2),
							new SetFingerRollers(SetFingerRollers.OFF, .1),
							new ResetRatchet()
							),
					new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
					new SequencedPause(.3),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new SequencedPause(.2),
					
					new SequencedMultipleItem(
							new DriveStraightLowAcceleration(81),
							new SetFingersPos(Fingers.OPEN)
							), 
					new SequencedMultipleItem(
							new CloseClapper(),
							new SetFingersPos(Fingers.PARALLEL), 
							new RetractRatchet(),
							new SetFingerRollers(SetFingerRollers.INTAKE, 1.5)
//							new DriveStraightLowAcceleration(10)
							), 
					new SequencedMultipleItem(
							new SequencedPause(0.2),
							new SetFingerRollers(SetFingerRollers.OFF, .1),
							new ResetRatchet()
							),
					new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
					new SequencedPause(0.1),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new SequencedPause(0.2),
					new SequencedMultipleItem(
							new DriveStraightLowAcceleration(81),
							new SetFingersPos(Fingers.OPEN)
							), 
					new SequencedMultipleItem(
							new CloseClapper(),
							new SetFingersPos(Fingers.PARALLEL), 
							new RetractRatchet(),
							new SetFingerRollers(SetFingerRollers.INTAKE, 1.5)
//							new DriveStraightLowAcceleration(10)
							), 
					new SequencedMultipleItem(
							new SequencedPause(0.2),
							new SetFingerRollers(SetFingerRollers.OFF, .1),
							new ResetRatchet()
							),
					new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
					new SequencedPause(0.1),
					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					
//					new RotateToAngle(0),
//					new OpenClapper(), //TODO make multiple
//					new DriveStraight(3),
							/*		new SequencedMultipleItem(
							new SetFingersPos(Fingers.PARALLEL), 
							new SetFingerRollers(SetFingerRollers.INTAKE, 1)
							), 
					new SequencedMultipleItem(
							new SequencedPause(0.2),
							new SetFingerRollers(SetFingerRollers.OFF, .1),
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT)
//							new RotateToAngle(15)
							),
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.LOADING_SETPOINT)
							),
//					new DriveStraight(81),
							
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
							new SequencedPause(0.1),
							new RotateToAngle(15)
							),
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.LOADING_SETPOINT),
							new DriveStraight(48)
							),					
					new RotateToAngle(0),
					new DriveStraight(6), */
			}); 

		case THREE_TOTE_PUSH_CONTAINERS: 
			return new Sequencer(new SequencedItem[] {
					new DriveStraight(81), 
					new SequencedPause(1), 
					new DriveStraight(81) 
			});

		case CONTAINER_AND_TOTE: 
			return new Sequencer(new SequencedItem[] {
					//first pick up tote
					new SequencedMultipleItem(
							new SetFingersPos(Fingers.CLOSED), 
							new SetFingerRollers(SetFingerRollers.INTAKE, 1)
							), 
					new SequencedMultipleItem(
							new SetFingersPos(Fingers.PARALLEL),
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
							new SetFingerRollers(SetFingerRollers.OFF, .1)
							), 
					new DriveStraight(20), //or whatever that is
					//then pick up container
					//no idea how to do that
					//then finish the auto and stuff

			});
			

			//			case CONTAINER_STEAL_LEFT: 
			//				return new Sequencer(new SequencedItem[] {
			//					new DriveStraight(60) 
			//				});
			//				
			//			case CONTAINER_STEAL_RIGHT: 
			//				return new Sequencer(new SequencedItem[] {
			//					new DriveStraight(60) 
			//				});
			//				
			//			case SECRET_CONTAINER_STEAL_START_LEFT: 
			//				return new Sequencer(new SequencedItem[] {
			//					new DriveStraight(60) 
			//				});
			//				
			//			case SECRET_CONTAINER_STEAL_START_RIGHT: 
			//				return new Sequencer(new SequencedItem[] {
			//					new DriveStraight(60) 
			//				});

		}
		return new Sequencer();
	}

	public static Sequencer createIntakeToteRoutine() {
		return new Sequencer(new SequencedItem[] {
			new SequencedMultipleItem(
					new CloseClapper(),
					new SetFingersPos(Fingers.PARALLEL), 
					new RetractRatchet(),
					new SetFingerRollers(SetFingerRollers.INTAKE, 1)
					), 
			new SequencedMultipleItem(
					new SequencedPause(0.2),
					new SetFingerRollers(SetFingerRollers.OFF, .1),
					new ResetRatchet()
					),
//			new SequencedMultipleItem(
//			new SetFingersPos(Fingers.PARALLEL),
			new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
//							),
			new SequencedPause(0.1),
			new MoveClapperVertically(Clapper.LOADING_SETPOINT)
		});
	}
	public static Sequencer createDropToteStackRoutine() {
		return new Sequencer(new SequencedItem[] {
			new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT), 
			new RetractRatchet(),
			new MoveClapperVertically(Clapper.LOADING_SETPOINT), 
			new OpenClapper(), 
			new ResetRatchet()
		});
	}
	
	public static Sequencer createContainerRightingRoutine() {
		double kP = Robot.clapper.getkP();
		double kI = Robot.clapper.getkI();
		double kD = Robot.clapper.getkD();
		return new Sequencer(new SequencedItem[] {
				new SetFingersPos(Fingers.CLOSED),
				new SequencedPause(0.4),
				new SetClapperPID(0.02, kI, kD),
				new MoveClapperVertically(Clapper.COOP_TWO_TOTES_SETPOINT), //TODO: find custom setpoint
				new SequencedPause(0.4),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SetFingersPos(Fingers.PARALLEL),
				new SetClapperPID(kP, kI, kD)
				
		});
	}
		public static Sequencer createIntakeToteRoutineBackup() {
			return new Sequencer(new SequencedItem[] {
				new SequencedMultipleItem(
						new OpenClapper(),
						new SetFingersPos(Fingers.CLOSED), 
						new RetractRatchet(),
						new SetFingerRollers(SetFingerRollers.INTAKE, 1),
						new DriveSlowConstant()
						), 
				new SequencedPause(.2),
				new SequencedMultipleItem(
//						new SetFingerRollers(SetFingerRollers.REVERSE, 1),
						new SetFingersPos(Fingers.PARALLEL),
						new ResetRatchet(),
						new DriveSlowConstant()
						),
				new CloseClapper(),
				new SequencedPause(.2),
//				new SetFingerRollers(SetFingerRollers.INTAKE, 1),
//				new SequencedMultipleItem(
//				new SetFingersPos(Fingers.PARALLEL),
//				new SequencedPause(1),
				new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
				new SetFingerRollers(SetFingerRollers.INTAKE, 1), // 0.2
//								),
				new SequencedPause(.2),
				new MoveClapperVertically(Clapper.LOADING_SETPOINT)
			});
		}
	}