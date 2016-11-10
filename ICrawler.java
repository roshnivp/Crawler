
package com.sjsu.crawler.core;

import com.sjsu.crawler.filter.ILinkFilter;
import com.sjsu.crawler.model.ICrawlerModel;
import com.sjsu.crawler.parser.IParser;

/**
 * All crawlers implement the common methods described below.
 *
 * 
 */
public interface ICrawler {

    /**
     * @return Returns the parser.
     */
    IParser getParser();

    /**
     * @param parser The parser to set.
     */
    void setParser(IParser parser);

    /**
     * @return Returns the crawler model.
     */
    ICrawlerModel getModel();

    /**
     * @param model The crawler model to set.
     */
    void setModel(ICrawlerModel model);

    /**
     * @return Returns the linkFilter.
     */
    ILinkFilter getLinkFilter();

    /**
     * @param linkFilter The linkFilter to set.
     */
    void setLinkFilter(ILinkFilter linkFilter);

    /**
     * Starts the crawling process. Before starting the crawling
     * process, the model and the parser have to be set.
     */
    void start();

}
