
package com.sjsu.crawler.filter;

/**
 * Defines a filter for links which should be further crawled.
 
 */
public interface ILinkFilter {

    /**
     * @param origin the complete URI of the page which contains the link
     * @param link the absolute path of the to be checked link
     *
     * @return true if the <code>link</code> should be followed and returns
     *         false if <code>link</code> is null.
     */
    boolean accept(String origin, String link);

}
