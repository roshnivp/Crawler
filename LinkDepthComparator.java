
package com.sjsu.crawler.link;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A link comparator which compares the depth first and then the URI.
 */
public final class LinkDepthComparator implements Comparator, Serializable {

	private static final long serialVersionUID = 7052071018705115422L;

    /**
     * {@inheritDoc}
     */
    public int compare(Object o1, Object o2) {
        LinkDepth l1 = (LinkDepth) o1;
        LinkDepth l2 = (LinkDepth) o2;

        if (l1.getDepth() != l2.getDepth()) {
            return l1.getDepth() - l2.getDepth();
        } else {
            return l1.getURI().compareTo(l2.getURI());
        }
    }

}
