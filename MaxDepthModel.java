
package com.sjsu.crawler.model;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sjsu.crawler.database.DB;
import com.sjsu.crawler.link.Link;
import com.sjsu.crawler.link.LinkDepth;
import com.sjsu.crawler.link.LinkDepthComparator;

/**
 * Simple graph model parser without registration in and out coming links. It
 * stops to a maximum depth given.
 
 */
public class MaxDepthModel implements ICrawlerModel {

    private static final transient Log LOG = LogFactory.getLog(MaxDepthModel.class);

    /** The default depth. */
    public static final int DEFAULT_MAX_DEPTH = 1;

    /** The max depth. */
    private int depth;

    /** The default number of iterations. */
  //  public static final int DEFAULT_MAX_ITERATIONS = 2040;

    /** The max iterations. */
    //private int iterations;

    /** A map of the visited links. */
    private HashMap visitedURIs = new HashMap();

    /** A set of the to be visited links. */
    private TreeSet toVisitURIs = new TreeSet(new LinkDepthComparator());

    /**
     * Constructor for Crawler which creates model with max depth of 4 and max
     * iterations of 2048.
     */
    public MaxDepthModel() {
        this(DEFAULT_MAX_DEPTH);
    }

    /**
     * Constructor for Crawler which creates model with max depth based of the
     * depth parameter and max iterations of 2048.
     *
     * @param depth the max depth.
     */
//    public MaxDepthModel(int depth) {
//        this(depth);
//    }

    /**
     * Constructor for Crawler which creates model with max depth based of the
     * depth parameter and max iterations based of the iterations parameter.
     *
     * @param depth the max depth.
     * @param iterations the max of iterations.
     */
   // public MaxDepthModel(int depth, int iterations) {
 	 public MaxDepthModel(int depth) {
      this.depth = depth;
       // this.iterations = iterations;

        LOG.debug("Crawler model: " + MaxDepthModel.class.getName());
        LOG.debug("- max depth=" + depth);
        //LOG.debug("- max iterations=" + iterations);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isEmpty() {
        // check if there is at least one link left
       // if ((toVisitURIs.size() == 0) || (iterations <= 0))
        	 if (toVisitURIs.size() == 0){
            return true;
        }

        // get the next element (first element in the set)
        LinkDepth l = (LinkDepth) toVisitURIs.first();

        return l.getDepth() > depth;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Link pop() {
        // check constraint
                if (toVisitURIs.size() == 0) {
            throw new NoSuchElementException("No more URIs in MaxDepthModel.");
        }

        // reduce the iterations without a check
       // iterations--;

        // get the next element and remove it from the list
        LinkDepth l = (LinkDepth) toVisitURIs.first();
        toVisitURIs.remove(l);

        // check constraint
        if (l.getDepth() > depth) {
            throw new NoSuchElementException("Max depth reached in MaxDepthModel.");
        }

        // mark this link as visited
        visitedURIs.put(l.getURI(), l);

        // return the link
        return l;
    }

    /**
     * {@inheritDoc}
     */
    public void add(Link origin, String uri) {
        int originDepth = getDepth(origin);
        try {
			addInternal(originDepth, uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * {@inheritDoc}
     */
    public void add(Link origin, Collection uri) {
        int originDepth = getDepth(origin);

        Iterator iter = uri.iterator();
        while (iter.hasNext()) {
            try {
				addInternal(originDepth, (String) iter.next());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    /**
     * {@inheritDoc}
     */
    public Collection getVisitedURIs() {
        return visitedURIs.values();
    }

    /**
     * {@inheritDoc}
     */
    public Collection getToVisitURIs() {
        return toVisitURIs;
    }

    // --- internal methods and/or classes ---

    /**
     * @param link the URI of the link
     * @return returns the depth of a visited uri.
     */
    private int getDepth(Link link) {
        if (link == null) {
            return -1;
        }

        LinkDepth l = (LinkDepth) visitedURIs.get(link.getURI());

        if (l != null) {
            return l.getDepth();
        } else {
            // this is the root
            return -1;
        }
    }

    /** HashMap to avoid that links are added more than once. */
    private HashMap foundLinks = new HashMap();

    /**
     * Adds a new URI to the to be visited list.
     *
     * @param originDepth the depth of the origin URI
     * @param uri the link of the new URI
     */
    private synchronized void addInternal(int originDepth, String uri) throws
    IOException{
    	
//    	try
//    	{
    		
    		
    		 String uriV = uri;
    		
         // find the link via the hashcode
        LinkDepth l = (LinkDepth) foundLinks.get(uri);

        // the depth of the uri
        final int depth = originDepth + 1;

        // is the link new
        if (l == null) {
            l = createLink(null, uri, depth);
            //insert into DB----------------------------------
            
            //System.out.println("URI passed: " +uri);
                      
//            String sql2 = "INSERT INTO ToVisitURIs ([TV_URL]) values" + "(?);";
//			PreparedStatement stmnt = DB.conn.prepareStatement(sql2);
//			stmnt.setString(1, uriV);
//			stmnt.execute();
//			
            foundLinks.put(uri, l);
            toVisitURIs.add(l);
        } else {
            // check if depth is to change
            if (depth < l.getDepth()) 
            		{
		                toVisitURIs.remove(l);		        
		                l.setDepth(depth);
		                toVisitURIs.add(l);
            		}
        		}
        
    	//}
    	
	 
    	 
    	
    }

    /**
     * {@inheritDoc}
     */
    public Link createLink(String originUri, String uri) {
        return createLink(originUri, uri, 0);
    }

    /**
     * Creates a <b>new</b> link based on the orginUri and uri parameter.
     *
     * @param originUri the origin URI of the link
     * @param uri the new URI
     * @param depth the depth of the link
     * @return the new created {@link LinkDepth} based on the parameters
     */
    public LinkDepth createLink(String originUri, String uri, int depth) {
        return new LinkDepth(uri, depth);
    }

}
