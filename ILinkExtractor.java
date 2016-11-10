
package com.sjsu.crawler.util;

import java.util.Collection;

import com.sjsu.crawler.filter.ILinkFilter;

/**
 * Defines an interface for a custom link extraction. The implementation which has to
 * be thread safe can be used in the SimpleHttpClientParser and FileSystemParser.
 * The HtmlParser has a build in link extraction.
 */
public interface ILinkExtractor {

    /**
     * @param url the url (origin) of the page
     * @param content the complete web page
     * @param linkFilter avoids adding links to the collection
     * @return a collection of links of type String found in the content
     */
    Collection retrieveLinks(String url, String content, ILinkFilter linkFilter);

}
