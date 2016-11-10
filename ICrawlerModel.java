
package com.sjsu.crawler.model;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.sjsu.crawler.link.Link;

/**
 * Defines the methods for the crawler model.
  */
public interface ICrawlerModel {

    /**
     * Returns <tt>true</tt> if the crawler model has no more links. (In other
     * words, returns <tt>false</tt> if <tt>pop</tt> would return an element
     * rather than throwing an exception.)
     * 
     * @return <tt>true</tt> if the crawler model has no more links.
     */
    boolean isEmpty();

    /**
     * Returns the next link in the crawler model. The method removes the link
     * from the to visit list and adds the link to the visited list.
     * 
     * @return the next link in the crawler model.
     * @exception NoSuchElementException crawler model has no more elements.
     */
    Link pop();

    /**
     * Adds a <b>new</b> URI to the to be visited URIs.
     * 
     * @param origin the origin link of the new URI
     * @param uri adds a new link with the URI to the crawler model.
     */
    void add(Link origin, String uri);

    /**
     * Adds <b>new</b> URIs to the to be visited URIs.
     * 
     * @param origin the origin link of the new URI
     * @param uri adds new links with the URIs (in the collection) to the
     *            crawler model.
     */
    void add(Link origin, Collection uri);

    /**
     * Creates a <b>new</b> link based on the orginUri and uri parameter.
     * 
     * @param originUri the origin URI of the link
     * @param uri the new URI
     * @return the new created {@link Link} based on the parameters
     */
    Link createLink(String originUri, String uri);

    /**
     * @return a object set of visited URIs.
     */
    Collection getVisitedURIs();

    /**
     * @return a object set of still to be visited URIs.
     */
    Collection getToVisitURIs();

}
