package org.togglz.core.util;

import java.util.Comparator;

/**
 * 
 * Common interface for classes that have different priorities.
 * 
 * @author Christian Kaltepoth
 * 
 */
public interface Weighted {

    int priority();

    public static class WeightedComparator implements Comparator<Weighted> {

        @Override
        public int compare(Weighted left, Weighted right) {
            return left.priority() - right.priority();
        }

    }
}