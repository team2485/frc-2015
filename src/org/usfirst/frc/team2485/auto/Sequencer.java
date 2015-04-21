package org.usfirst.frc.team2485.auto;

import java.util.Vector;

import org.usfirst.frc.team2485.auto.SequencedPause;

/**
 * Provides a utility to sequence events and actions.
 * @author Bryce Matsumori
 *
 * @see SequencedItem
 */
public class Sequencer {
    private final Vector sequenced = new Vector();
    private boolean started = false;
    private long startTime;
    private int currIndex;
    private long currIndexStartTime;

    private static Vector sequencers = new Vector();

    /**
     * Creates a new {@code Sequencer} with no sequenced items.
     */
    public Sequencer() {}

    /**
     * Creates a new {@code Sequencer} with the specified sequence's items.
     * @param other the other sequence
     */
    public Sequencer(Sequencer other) {
        this(other.asArray());
    }

    /**
     * Creates a new {@code Sequencer} with the specified initial {@code SequencedItem}.
     * @param initial the item to sequence
     *
     * @see SequencedItem
     * @see SequencedPause
     */
    public Sequencer(SequencedItem initial) {
        sequenced.addElement(initial);
    }

    /**
     * Creates a new {@code Sequencer} with the specified initial {@code SequencedItem}s.
     * @param initial the items to sequence
     *
     * @see SequencedItem
     * @see SequencedPause
     */
    public Sequencer(SequencedItem[] initial) {
        for (int i = 0; i < initial.length; i++) 
            sequenced.addElement(initial[i]);
    }

    /**
     * Gets the sequenced items in {@code SequencedItem[]} array form.
     * @return an array containing the sequenced items in order
     *
     * @see SequencedItem
     */
    public SequencedItem[] asArray() {
        SequencedItem[] export = new SequencedItem[sequenced.size()];
        sequenced.copyInto(export);
        return export;
    }

    /**
     * Schedules the specified item to be run after all previous items have been run.
     * @param item the item to schedule
     * @return A reference to this {@code Sequencer} for method chaining.
     *
     * @see SequencedItem
     * @see SequencedPause
     */
    public Sequencer add(SequencedItem item) {
        sequenced.addElement(item);
        return this;
    }

    /**
     * Schedules the specified item(s) to be run after all previous items have been run.
     * Specified items will be added in order.
     * @param items the items to schedule
     * @return A reference to this {@code Sequencer} for method chaining.
     *
     * @see SequencedItem
     * @see SequencedPause
     */
    public Sequencer add(SequencedItem[] items) {
        for (int i = 0; i < items.length; i++) 
            sequenced.addElement(items[i]);
        return this;
    }

    /**
     * Gets the {@code SequencedItem} scheduled at the specified index.
     * @param index the index of the item
     * @return the item at the specified index
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     * @see SequencedItem
     */
    public SequencedItem get(int index) {
        return (SequencedItem)sequenced.elementAt(index);
    }

    /**
     * Gets the number of {@code SequencedItem}s.
     * @return the total count of items
     *
     * @see SequencedItem
     */
    public int count() {
        return sequenced.size();
    }

    /**
     * Clears all of the {@code SequencedItem}s.
     * The result of this operation will be that no items are sequenced.
     * @return A reference to this {@code Sequencer} for method chaining.
     *
     * @see SequencedItem
     */
    public Sequencer clear() {
        sequenced.removeAllElements();
        return this;
    }

    /**
     * Starts a run by setting run state variables.
     */
    private void start() {
        started = true;
        startTime = System.currentTimeMillis();
        currIndex = 0;
        currIndexStartTime = startTime;
    }

    /**
     * Runs the sequenced items in order according to their durations.
     * This does not need to be run in a precise interval, but it should be run
     * frequently enough for maximum timing accuracy.
     *
     * @see SequencedItem
     * @return {@code true} when finished, otherwise {@code false}.
     */
    public boolean run() {
        if (!started) start();

        if (currIndex >= sequenced.size())
            return true; // finished

        final long currTime = System.currentTimeMillis();
        final SequencedItem currItem = (SequencedItem)sequenced.elementAt(currIndex);
        final long currIndexTime = currTime - currIndexStartTime;

        // check if time is greater than duration (convert to ms from s)
        if (currIndexTime > (long)(currItem.duration()*1000)) {
            // current duration exceeded, continue to next
            currIndex++;
            currIndexStartTime = currTime;

            if (currIndex >= sequenced.size())
                return true; // finished

            // execute next
            ((SequencedItem)sequenced.elementAt(currIndex)).run();
        }
        else 
            currItem.run();
        
        return false;
    }

    /**
     * Resets the currently executing sequence so that this {@code Sequencer}
     * starts executing at the first {@code SequencedItem}.
     */
    public void reset() {
        start();
    }

    /**
     * Gets the time elapsed since the run starting.
     * @return the time, in seconds
     */
    public double elapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }

    /**
     * Sets whether this Sequencer should run when {@code Sequencer.runAll()} is called.
     * @param enabled Run automatically?
     * @return A reference to this {@code Sequencer} for method chaining.
     */
    public Sequencer auto(boolean enabled) {
        if (enabled) if (!sequencers.contains(this)) sequencers.addElement(this);
        else sequencers.removeElement(this);
        return this;
    }

    /**
     * Creates a shallow copy of this {@code Sequencer}'s {@code SequencedItem}s.
     * @return a copy
     */
    protected Object clone() {
        Sequencer newSequencer = new Sequencer(this.asArray());
        return newSequencer;
    }

    /**
     * Runs all the enabled {@code Sequencer}s.
     * Use this instead of {@code run()}ning each {@code Sequencer} individually.
     */
    public static void runAll() {
        for (int i = 0; i < sequencers.size(); i++) {
            Object e = sequencers.elementAt(i);
            if (e != null) ((Sequencer)e).run();
        }
    }
}
