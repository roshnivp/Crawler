
package com.sjsu.crawler.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sjsu.crawler.filter.ILinkFilter;

/**
 * Helpful methods for links extraction and implementation of ILinkExtractor.
 
 */
public final class LinksUtil {

    private static final transient Log LOG = LogFactory.getLog(LinksUtil.class);

    /**
     * Default ILinkExtractor implementation to extract the links in a page.
     * This is used in SimpleHtmlParser and FileSystemParser.
     
     */
    public static final ILinkExtractor DEFAULT_LINK_EXTRACTOR = new ILinkExtractor() {
        public Collection retrieveLinks(String url, String content, ILinkFilter linkFilter) {
            return LinksUtil.retrieveLinks(url, content, linkFilter);
        }
    };

    /**
     * Disallow creation of utility class.
     */
    private LinksUtil() {
    }

    /**
     * @param currentLink
     *            the current page in which the new link is contained
     * @param newLink
     *            the to be completed link
     * @return a full qualified link or <code>null</code> if the newLink can't
     *         be parsed.
     */
    public static String getURI(String currentLink, String newLink) {
        try {
            URI base = new URI(currentLink, false);
            return getURI(base, newLink);
        } catch (URIException e) {
            LOG.info("URI problem with current link '" + currentLink + '\'', e);
            return null;
        }
    }

    /**
     * @param currentURI
     *            the current page URI in which the new link is contained
     * @param newLink
     *            the to be completed link
     * @return a full qualified link or <code>null</code> if the newLink can't
     *         be parsed.
     */
    public static String getURI(URI currentURI, String newLink) {
        if (newLink == null) {
            return null;
        }

        try {
            // workaround for http:/path/example.htm
            if (!newLink.startsWith("http://") && newLink.startsWith("http:/")) {
                newLink = newLink.substring(5);
            }

            // workaround for https:/path/example.htm
            if (!newLink.startsWith("https://") && newLink.startsWith("https:/")) {
                newLink = newLink.substring(6);
            }

            // create new URIs
            // fix for felix's escaping issue of "%C3%96", use true instead of false here
            // TODO only unescape URI once in the processing
            URI newURI = new URI(currentURI, newLink, true);

            // ignore the schemes other than http
            if (!"http".equals(newURI.getScheme()) && !"https".equals(newURI.getScheme())) {
                return null;
            }

            return newURI.toString();
        } catch (URIException e) {
            LOG.info("URI problem with current link '" + currentURI.toString() + "' and new link '" + newLink + '\'', e);
            return null;
        }
    }
    
    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes, only if Apache Commons Lang is available in the
     * classpath. Supports HTML 4.0 entities.</p>
     *
     * @param str  the <code>String</code> to unescape, may be null
     * @return a new unescaped <code>String</code> if Apache Common Lang is available or the same escaped <code>String</code>,
     *         <code>null</code> if null string input
     * @since 1.3
     */
    public static String unescapeHtmlCommonsLang(String str) {
        try {
            Class.forName(StringEscapeUtils.class.getName());
            return StringEscapeUtils.unescapeHtml(str);
        } catch (ClassNotFoundException ex) {
            // ignore and fall back without unescaping
        } catch (NoClassDefFoundError e) {
            // ignore and fall back without unescaping
        }
        return str;
    }

    private static final String[][] TAGS = { { "<a", "href=\"" }, { "<frame ", "src=\"" }, { "<iframe ", "src=\"" } };

    /**
     * @see com.sjsu.crawler.util.ILinkExtractor#retrieveLinks(java.lang.String,
     *      java.lang.String, com.sjsu.crawler.filter.ILinkFilter)
     */
    public static Collection retrieveLinks(final String url, final String content, final ILinkFilter linkFilter) {
        URI base = null;
        try {
            base = new URI(url, false);
        } catch (URIException e) {
            LOG.info("Can't create URI for current link '" + url + '\'', e);
            return Collections.EMPTY_LIST;
        }

        // FIXME possible performance and memory improvement
        String pageLower = content.toLowerCase();

        Collection result = new HashSet();

        // find links
        for (int i = 0; i < TAGS.length; i++) {
            final String tag = TAGS[i][0];
            final String attribute = TAGS[i][1];
            int pos = 0;

            while (pos < content.length()) {
                final int begin = pageLower.indexOf(tag, pos);
                if (begin > -1) {
                    int start = pageLower.indexOf(attribute, begin);
                    if (start > -1) {
                        // create a full qualified link
                        start += attribute.length();
                        final int end = content.indexOf('\"', start);
                        // Support of HTML escaped links when Apache Commons Lang 2.4 is installed
                        String link = LinksUtil.getURI(base, unescapeHtmlCommonsLang(content.substring(start, end)));

                        // if no filter is set or a set filter accepts the link,
                        // then add it to the list
                        if ((link != null) && ((linkFilter == null) || (linkFilter.accept(url, link)))) {
                            result.add(link);
                        }

                        // next parsing position
                        pos = end + 1;
                    } else {
                        // ignore a tag, because tag wasn't found
                        pos = begin + 1;
                    }
                } else {
                    // end parsing
                    pos = content.length();
                }
            }

        }

        return result;
    }

}
