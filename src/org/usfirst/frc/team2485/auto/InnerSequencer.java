package org.usfirst.frc.team2485.auto;

/**
 * Runs a {@code Sequencer} as a {@code SequencedItem}.
 * The sequenced item runs until the sequencer has finished.
 * @author Bryce Matsumori
 *
 * @see Sequencer
 * @see SequencedItem
 */
public final class InnerSequencer implements SequencedItem {
    private final Sequencer sequencer;
    private boolean finished = false;

    /**
     * Creates a new {@code InnerSequencer} using the specified sequencer.
     * @param sequencer the sequencer to run
     */
    public InnerSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    /**
     * Runs the sequencer.
     */
    public void run() {
        finished = sequencer.run();
    }

    /**
     * Returns {@code 0} if the sequencer is finished, otherwise an indefinite value.
     * @return a double
     */
    public double duration() {
        return finished ? 0 : Double.MAX_VALUE;
    }
}