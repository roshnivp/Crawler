
package com.sjsu.crawler.parser;

import com.sjsu.crawler.link.Link;

/**
 * Defines an abstract container for the downloaded page.
 */
public abstract class PageData {

    /** Status not loaded yet of the data. */
    public static final int NOT_LOADED = 0;
    /** Status data loaded without any errors. */
    public static final int OK = 1;
    /** Status data couldn't loaded due to unexpected errors. */
    public static final int ERROR = 2;
    /** Status data wasn't modified. */
    public static final int NOT_MODIFIED = 3;
    /** Status data contains a redirect. */
    public static final int REDIRECT = 4;
    
    /** The link of this data. */
    private Link link;

    /** The status of the data. */
    private int status;

    /**
     * The default constructor.
     *
     * @param link the link of the page data.
     */
    public PageData(Link link) {
        this(link, NOT_LOADED);
    }

    /**
     * The default constructor.
     *
     * @param link the link of the page data.
     * @param status the status of the data
     */
    public PageData(Link link, int status) {
        this.link = link;
        this.status = status;
    }

    /**
     * @return Returns the link.
     */
    public Link getLink() {
        return link;
    }

    /**
     * @return Returns the status, e.g. NOT_LOADED, ERROR, NOT_MODIFIED or OK.
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status The status to set of the downloaded data
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return Returns the data of the page.
     */
    public abstract Object getData();
    
    /**
     * @param data set the data of the page.
     * @since 1.3
     */
    public abstract void setData(Object data);

}
