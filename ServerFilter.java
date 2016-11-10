
package com.sjsu.crawler.filter;

/**
 * A ILinkFilter that filters URIs for a special server only.
 */
public class ServerFilter implements ILinkFilter {

    /** The specified server to filter for. */
    private String server;

    /**
     * Constructor for ServerFilter.
     *
     * @param server the server to filter for.
     */
    public ServerFilter(String server) {
        this.server = server;
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept(String origin, String link) {
        return (link != null) && (link.startsWith(server));
    }

}
