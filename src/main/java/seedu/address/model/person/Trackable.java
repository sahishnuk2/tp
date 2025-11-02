package seedu.address.model.person;

import java.util.List;

/**
 * Represents a data structure wrapper that is tracked on the person card.
 */
public interface Trackable {
    List<TrackerColour> getTrackerColours();

    /**
     * Returns the list of label texts representing each tracked item.
     * (e.g., ["EX1", "EX2", ...] for exercises)
     */
    List<String> getLabels();
}
