
package com.sjsu.crawler.util;

/**
 * A stop watch for milliseconds
 */
public final class StopWatch {

    /** the total time in milliseconds. */
    private long total;

    /** the start time in milliseconds. */
    private long start;

    /**
     * Starts the stop watch.
     */
    public void start() {
        start = System.currentTimeMillis();
    }

    /**
     * Stops the stop watch.
     * 
     * @return the millis between the last start and current stop of this watch.
     */
    public long stop() {
        long diff = System.currentTimeMillis() - start;
        total += diff;
        return diff;
    }

    /**
     * Resets the stop watch to 0.
     */
    public void reset() {
        total = 0;
    }

    /**
     * @return returns the stopped total time in milliseconds.
     */
    public long getTime() {
        return total;
    }

}
