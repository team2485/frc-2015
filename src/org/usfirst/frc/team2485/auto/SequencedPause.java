package org.usfirst.frc.team2485.auto;


/**
 * Represents a pause in a sequence. The sequencer will do nothing for the
 * specified duration.
 *
 * @author Bryce Matsumori
 *
 * @see Sequencer
 * @see SequencedItem
 */
public final class SequencedPause implements SequencedItem {
    private final double duration;

    /**
     * Initializes a {@code SequencedPause} with the specified duration.
     * @param duration
     */
    public SequencedPause(double duration) {
        this.duration = duration;
    }

    /**
     * Returns the duration of the pause, in seconds.
     * @return the duration, in seconds
     */
    public double duration() {
        return duration;
    }

    /**
     * Does nothing.
     */
    public void run() {
    }
}
