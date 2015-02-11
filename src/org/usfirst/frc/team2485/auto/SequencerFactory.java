package org.usfirst.frc.team2485.auto;

import org.usfirst.frc.team2485.auto.SequencedItems.AutoTestPrint;


public class SequencerFactory {

	//auto types
	public static final int 
	SEQ_TEST = -1; 

	public static Sequencer createAuto(int autoType) {

		switch (autoType) {
		case SEQ_TEST: 
			return new Sequencer(new SequencedItem[] {
					new AutoTestPrint() 
			}); 
		}
		return new Sequencer();
	}
}