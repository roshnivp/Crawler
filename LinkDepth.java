
package com.sjsu.crawler.link;

/**
 * A link contains the uri and the depth of the link.
 *
  */
public class LinkDepth extends Link {

    /** the depth of the link. */
    private int depth;

    /**
     * @param uri the URI of the link
     * @param depth the depth of the link
     */
    public LinkDepth(String uri, int depth) {
        super(uri);
        this.depth = depth;
    }

    /**
     * @return Returns the depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @param depth The depth to set.
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return super.toString() + " [" + depth + ']';
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        // Avoid FindBugs Dodgy Warning
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        // Avoid FindBugs Bad Practise Warning
        return super.hashCode();
    }

}
