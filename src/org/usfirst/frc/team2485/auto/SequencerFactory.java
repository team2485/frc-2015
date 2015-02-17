package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.auto.SequencedItems.AutoTestPrint;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClapper;
import org.usfirst.frc.team2485.auto.SequencedItems.CloseClaw;
import org.usfirst.frc.team2485.auto.SequencedItems.DisableClawPID;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveSlowConstant;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraight;
import org.usfirst.frc.team2485.auto.SequencedItems.DriveStraightLowAcceleration;
import org.usfirst.frc.team2485.auto.SequencedItems.ExtendRatchet;
import org.usfirst.frc.team2485.auto.SequencedItems.IncrementToteCount;
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
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingerRollers;
import org.usfirst.frc.team2485.auto.SequencedItems.SetFingersPos;
import org.usfirst.frc.team2485.robot.Robot;
import org.usfirst.frc.team2485.subsystems.Clapper;
import org.usfirst.frc.team2485.subsystems.Claw;
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
							new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1)
							), 
							new SequencedPause(0.7),
							//					new SequencedMultipleItem(
							//					new SetFingersPos(Fingers.PARALLEL),
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
							new SetFingerRollers(SetFingerRollers.OFF, .1, 0),
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
					new CloseClapper(), 
					new SequencedMultipleItem(
							new DriveSlowConstant(),
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT)
							),
					new SetFingersPos(Fingers.PARALLEL), 
					new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1),
					new SequencedPause(0.2),
					//					new SequencedMultipleItem(

					new MoveClapperVertically(Clapper.LOADING_SETPOINT),
					new DriveStraight(81),
					//							new OpenClapper()
					//					),
					new SequencedMultipleItem(
							new OpenClapper(),
							new SetFingersPos(Fingers.CLOSED), 
							new RetractRatchet(),
							new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1),
							new DriveSlowConstant()
							),
							new SequencedPause(.2),
							new SequencedMultipleItem(
									//							new SetFingerRollers(SetFingerRollers.REVERSE, 1),
									new SetFingersPos(Fingers.PARALLEL),
									new ExtendRatchet(),
									new DriveSlowConstant()
									),
									new CloseClapper(),
									new SequencedPause(.2),
									//					new SetFingerRollers(SetFingerRollers.INTAKE, 1),
									//					new SequencedMultipleItem(
									//					new SetFingersPos(Fingers.PARALLEL),
									//					new SequencedPause(1),
									new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
									new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1), // 0.2
									new SequencedPause(.2),
									new MoveClapperVertically(Clapper.LOADING_SETPOINT),
									new DriveStraightLowAcceleration(81),
									new SequencedMultipleItem(
											new OpenClapper(),
											new SetFingersPos(Fingers.CLOSED), 
											new RetractRatchet(),
											new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1),
											new DriveSlowConstant()
											),
									new SequencedPause(.2),
									new SequencedMultipleItem(
													//							new SetFingerRollers(SetFingerRollers.REVERSE, 1),
										new SetFingersPos(Fingers.PARALLEL),
										new ExtendRatchet(),
										new DriveSlowConstant()
										),
										new CloseClapper(),
										new SequencedPause(.2),
													//					new SetFingerRollers(SetFingerRollers.INTAKE, 1),
													//					new SequencedMultipleItem(
													//					new SetFingersPos(Fingers.PARALLEL),
													//					new SequencedPause(1),
										new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
										new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1), // 0.2
										new SequencedPause(.2),
										new MoveClapperVertically(Clapper.LOADING_SETPOINT)
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
							new SetFingerRollers(SetFingerRollers.INTAKE, 1, 1)
							), 
							new SequencedMultipleItem(
									new SetFingersPos(Fingers.PARALLEL),
									new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
									new SetFingerRollers(SetFingerRollers.OFF, .1, 0)
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


	public static Sequencer createToteIntakeNoLift(){
		return new Sequencer(new SequencedItem[] {
				new MoveClapperVertically(Clapper.LOADING_SETPOINT),
				new SequencedMultipleItem( 
						new CloseClapper(),
						new SetFingersPos(Fingers.CLOSED),
						new RetractRatchet(),
						new SetFingerRollers(SetFingerRollers.INTAKE, 2, 1)
				),
				new SequencedMultipleItem(
						new SetFingerRollers(SetFingerRollers.OFF, .1, 0),
						new CloseClapper(),
						new SetFingersPos(Fingers.PARALLEL)
				)
		});	
	}
	
	public static Sequencer createToteLift() {
		double kP = Robot.clapper.getkP();
		double kI = Robot.clapper.getkI();
		double kD = Robot.clapper.getkD();
		return new Sequencer(new SequencedItem[] {
				new IncrementToteCount(),
				new SequencedPause(0.25),
				new SequencedMultipleItem(	
						new ExtendRatchet(), 
						new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
						new MoveClawWithToteIntake(),
						new SetFingerRollers(SetFingerRollers.INTAKE, .5, .5)
				),
				new SequencedPause(0.25),
//				new SetClapperPID(0.0005, kI, kD),
				//try to stay in place but with a weak motor value...which lets gravity pull the stack down slowly
				//new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
				//alternative attempt...set to manual with a small positive value that can't hold the load
//				new MoveClapperManually(.15, .5),
				//new SetClapperPID(kP, kI, kD),
				new SetClapperPIDByToteCount(),
				new SetFingerRollers(SetFingerRollers.OFF, .05, 0),
				new SequencedPause(0.1),
				new MoveClapperVertically(Clapper.HOLDING_TOTE_SETPOINT)
		});
	}

	public static Sequencer createDropToteStackRoutine(boolean withToteOnFloor) { //tune kp down a bit? add a seq pause? 
		// have tote underneath (corraled OR not (check this)) or no tote underneath (all on hook)
		
		Sequencer returnSequence = null; 
		
		if (withToteOnFloor)
			returnSequence = new Sequencer(new SequencedItem[] {
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
							new MoveClawRelativeToClapper(Clapper.ABOVE_RATCHET_SETPOINT) ),
					new RetractRatchet(),
//					new DisableClawPID(),
//					new OpenClaw(), 
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.DROP_OFF_POS_ON_ONE_TOTE), 
							new MoveClawRelativeToClapper(Clapper.DROP_OFF_POS_ON_ONE_TOTE) ),		
					new OpenClaw(),
					new OpenClapper() 
			});
		else {
			returnSequence = new Sequencer(new SequencedItem[] {
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.ABOVE_RATCHET_SETPOINT),
							new MoveClawRelativeToClapper(Clapper.ABOVE_RATCHET_SETPOINT) ), 
					new RetractRatchet(),
//					new DisableClawPID(),
//					new OpenClaw(), 
					new SequencedMultipleItem(
							new MoveClapperVertically(Clapper.LOADING_SETPOINT), // figure out if this should be new setpoint that is higher
							new MoveClawRelativeToClapper(Clapper.LOADING_SETPOINT) ),		
					new OpenClaw(),
					new OpenClapper() 
			});
			}
		Robot.toteCounter.resetCount(); 
		return returnSequence;
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
	
	public static Sequencer createContainerPickupRoutine() {
		return new Sequencer(new SequencedItem[] {
				new CloseClaw(),
				new MoveClawVertically(Claw.ONE_TOTE_LOADING),
		}); 
	}
	
	public static Sequencer createPrepareForContainerLiftRoutine() {
		return new Sequencer(new SequencedItem[] {
				new SequencedMultipleItem(
						new OpenClapper(),
						new RetractRatchet(),
						new SetFingersPos(Fingers.OPEN),
						new OpenClaw(),
						new MoveClawVertically(Claw.CONTAINER_LOADING_POINT),
						new MoveClapperVertically(Clapper.LOADING_SETPOINT)
					)
				});
	}
}
