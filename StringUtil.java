
package com.sjsu.crawler.util;

/**
 * Provides common string utility methods.
 */
public final class StringUtil {

    /**
     * Disallow creation of utility class.
     */
    private StringUtil() {
    }

    /**
     * @param s the string to be checked
     * @return true if the string parameter contains at least one element
     */
    public static boolean hasLength(String s) {
        return (s != null) && (s.length() > 0);
    }

}
