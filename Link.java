
package com.sjsu.crawler.link;

/**
 * Contains the URI and timestamp of the link.
 */
public class Link implements Comparable {

    /** the URI of the link. */
    private String uri;
    
    /** the modification timestamp of the link. */
    private long timestamp = -1L;

    /**
     * @param uri the URI of the link
     */
    public Link(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Parameter uri is null.");
        }
        this.uri = uri;
    }

    /**
     * @param uri the URI of the link
     * @param timestamp the modification timestamp of the link
     * @since 1.3
     */
    public Link(String uri, long timestamp) {
        this(uri);
        this.timestamp = timestamp;
    }

    /**
     * @return Returns the link.
     */
    public String getURI() {
        return uri;
    }

    /**
     * @param uri The URI to set.
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * The modification of the timestamp allows the crawler process to avoid
     * downloading the whole content. The timestamp can be set with the
     * {@link com.sjsu.crawler.events.ILoadingEventListener} before the
     * parsing process is started.
     *
     * @return the modified timestamp of the link or <code>-1L</code> if not set
     * @since 1.3
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp sets the modified timestamp of the link
     * @since 1.3
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (obj instanceof Link) {
            Link l = (Link) obj;
            // TODO maybe check timestamp also
            return uri.equals(l.getURI());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        // TODO maybe use timestamp also
        return uri.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return uri;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {
        Link l = (Link) obj;
        // TODO maybe check timestamp also
        return uri.compareTo(l.getURI());
    }

}
