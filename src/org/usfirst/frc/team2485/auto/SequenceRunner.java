package org.usfirst.frc.team2485.auto;

/**
 * Runs a {@code Sequencer} in a separate {@code Thread}.
 *
 * Sequencers by themselves don't run continuously, so a thread like this one
 * is needed to continuously execute the sequence.
 *
 * @author Bryce Matsumori
 */
public class SequenceRunner extends Thread {
    private final Sequencer sequencer;
    private long intervalMillis;
    private boolean finished = false, stopped = false;

    /**
     * Creates a new {@code SequenceRunner} on the specified {@code Sequencer}
     * and with the specified interval between {@code Sequencer.run()}s.
     * @param sequencer the sequence to use
     * @param interval  the interval, in seconds
     */
    public SequenceRunner(Sequencer sequencer, double interval) {
        this.sequencer      = sequencer;
        this.intervalMillis = (long)(interval * 1000);
    }

    /**
     * Runs the sequence.
     */
    public void run() {
        finished = false;
        stopped  = false;

        // continue until finished (returns true)
        while (!stopped && !sequencer.run()) {
            try {
                // pause for interval
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) { }
        }
        if (!stopped) finished = true;
    }

    /**
     * Obtains the {@code Sequencer} being run.
     * @return the sequence
     */
    public Sequencer getSequencer() {
        return sequencer;
    }

    /**
     * Gets the interval between {@code Sequencer.run()}s.
     * @return the interval, in seconds
     */
    public double getInterval() {
        return intervalMillis * 0.001;
    }

    /**
     * Sets the interval between {@code Sequencer.run()}s.
     * @param interval the interval, in seconds
     */
    public void setInterval(double interval) {
        this.intervalMillis = (long)(interval * 1000);
    }

    /**
     * Stops the {@code SequenceRunner} elegantly.
     */
    public void shutdown() {
        this.stopped = true;
    }

    /**
     * Gets whether this runner has finished normally (i.e. not stopped).
     * @return a boolean
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Gets whether this runner has been shutdown.
     * @return a boolean
     */
    public boolean isShutdown() {
        return stopped;
    }
}