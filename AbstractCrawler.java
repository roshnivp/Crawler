/*
 *
 */
package com.sjsu.crawler.core;

import java.util.Collection;

import javax.swing.event.EventListenerList;

import com.sjsu.crawler.filter.ILinkFilter;
import com.sjsu.crawler.link.Link;
import com.sjsu.crawler.model.ICrawlerModel;
import com.sjsu.crawler.parser.IParser;
import com.sjsu.crawler.parser.PageData;

/**
 * Implements the common needed crawler methods.
 
 */
public abstract class AbstractCrawler implements ICrawler {

    /** The loader and parser of the pages. */
    protected IParser parser;

    /** The crawler model. */
    protected ICrawlerModel model;

    /** The link filter for the links. */
    protected ILinkFilter linkFilter;
    
    /** A list of all registered event listeners for this crawler. */
    private final EventListenerList listenerList = new EventListenerList();

    /**
     * @see com.sjsu.crawler.core.ICrawler#getParser()
     */
    public IParser getParser() {
        return parser;
    }

    /**
     * @see com.sjsu.crawler.core.ICrawler#setParser(com.sjsu.crawler.parser.IParser)
     */
    public void setParser(IParser parser) {
        this.parser = parser;
    }

    /**
     * @see com.sjsu.crawler.core.ICrawler#getModel()
     */
    public ICrawlerModel getModel() {
        return model;
    }

    /**
     * @see com.sjsu.crawler.core.ICrawler#setModel(com.sjsu.crawler.model.ICrawlerModel)
     */
    public void setModel(ICrawlerModel model) {
        this.model = model;
    }

    /**
     * @see com.sjsu.crawler.core.ICrawler#getLinkFilter()
     */
    public ILinkFilter getLinkFilter() {
        return linkFilter;
    }

    /**
     * @see com.sjsu.crawler.core.ICrawler#setLinkFilter(com.sjsu.crawler.filter.ILinkFilter)
     */
    public void setLinkFilter(ILinkFilter linkFilter) {
        this.linkFilter = linkFilter;
    }


    /**
     * Notify all listeners that have registered interest for notification on
     * this event type.
     *
     * @param link the link of the PageData object.
     */
    protected void fireBeforeLoadingEvent(Link link) {
        if (listenerList.getListenerCount() == 0) {
            return;
        }

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

    }
    
    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. Be aware that due to redirects <code>link</code> may 
     * not be equal to <code>pageData.getLink()</code>.
     *
     * @param link the link of the PageData object.
     * @param pageData the PageData of the loaded link
     */
    protected void fireAfterLoadingEvent(Link link, PageData pageData) {
        if (listenerList.getListenerCount() == 0) {
            return;
        }

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. Be aware that due to redirects <code>link</code> may 
     * not be equal to <code>pageData.getLink()</code>.
     *
     * @param link the link of the PageData object.
     * @param pageData the PageData object to fire for.
     * @param outgoingLinks the outgoing filtered links of the page.
     */
    protected void fireParserEvent(Link link, PageData pageData, Collection outgoingLinks) {
        if (listenerList.getListenerCount() == 0) {
            return;
        }

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
    }

    /**
     * @param pageData the {@link PageData} object to be checked
     * @return true if the page data return status is OK, not modified or is a redirect
     */
    protected boolean isPageDataOK(PageData pageData) {
        switch (pageData.getStatus()) {
            case PageData.OK:
            case PageData.NOT_MODIFIED:
            case PageData.REDIRECT:
                return true;
            default:
                return false;
        }
    }

}
