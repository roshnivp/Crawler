
package com.sjsu.crawler.parser.httpclient;

import com.sjsu.crawler.link.Link;
import com.sjsu.crawler.parser.PageData;

/**
 * A special page data container for the SimpleHttpClientParser which contain
 * the character set also.
  */
public class PageDataHttpClient extends PageData {

    /** the data of the page. */
    private String data;

    /** last modified date of the page. */
    private long lastModified = -1L;

    /** the charSet of the data. */
    private String charSet;

    /** 
     * standard last modified header. 
     * @deprecated use HttpClientUtil.HEADER_LAST_MODIFIED, constant will be removed with version 2.0
     */
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";

    /**
     * @param link the URI of the data
     * @param data the data of the URI
     * @param lastModified the last modification date of the data
     * @param charSet the character set of the data
     * @deprecated lastModified should be set in the link object now, constructor will be removed with version 2.0
     */
//    public PageDataHttpClient(final Link link, final String data, final long lastModified, final String charSet) {
//        super(link, PageData.OK);
//        this.data = data;
//        this.lastModified = lastModified;
//        this.charSet = charSet;
//    }

    /**
     * @param link the URI of the data
     * @param data the data of the URI
     * @param charSet the character set of the data
     * @since 1.3
     */
    public PageDataHttpClient(final Link link, final String data, final String charSet) {
        super(link, PageData.OK);
        this.data = data;
        this.charSet = charSet;
        // setting lastModified for backward compatibility
        this.lastModified = link.getTimestamp();
    }
    
    /**
     * @param link the URI of the data
     * @param status the status
     */
    public PageDataHttpClient(final Link link, final int status) {
        super(link, status);
    }
    
    /**
     * {@inheritDoc}
     * @see com.sjsu.crawler.parser.PageData#getData()
     */
    public final Object getData() {
        return data;
    }

    /**
     * Sets the data which has to be a String object.
     * {@inheritDoc}
     * @see com.sjsu.crawler.parser.PageData#setData(java.lang.Object)
     */
    public void setData(Object data) {
        this.data = (String) data;
    }

    /**
     * Returns the last modified date of the data.
     *
     * @return the last modified date.
     * 
     * @deprecated get lastModified via the link and it's timestamp property directly
     */
    public final long getLastModified() {
        return lastModified;
    }

    /**
     * Returns the character encoding of data.
     *
     * @return String The character set.
     */
    public final String getCharSet() {
        return charSet;
    }

}
