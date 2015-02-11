package org.usfirst.frc.team2485.auto;

/**
 * Contains an array of sequenced items, which are run in parallel.
 * All items are run until all finish.
 * @author W.A.R.Lords
 */
public class SequencedMultipleItem implements SequencedItem {
    private final SequencedItem[] items;

    public SequencedMultipleItem(SequencedItem[] items) {
        this.items = items;
    }

    public void run() {
        for (int i = 0; i < items.length; i++) {
            items[i].run();
        }
    }

    public double duration() {
        double max = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].duration() > max)
                max = items[i].duration();
        }

        return max;
    }
}