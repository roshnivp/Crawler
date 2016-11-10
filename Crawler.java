/*
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.sjsu.crawler;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sjsu.crawler.core.AbstractCrawler;
import com.sjsu.crawler.link.Link;
//import com.sjsu.crawler.model.MaxIterationsModel;
import com.sjsu.crawler.parser.PageData;
//import com.sjsu.crawler.parser.httpclient.SimpleHttpClientParser;
import com.sjsu.crawler.util.StopWatch;

/**
 * Project: Crawler
 *
 * 
 */
public class Crawler extends AbstractCrawler {

	private static final transient Log LOG = LogFactory.getLog(Crawler.class);

	private StopWatch total = new StopWatch();
	private StopWatch loading = new StopWatch();
	private StopWatch parsing = new StopWatch();
	private StopWatch listener = new StopWatch();

	/**
	 * Constructor for Crawler.
	 */
	public Crawler() {
		super();
	}

	/**
	 * Starts the crawling process in a single thread.
	 * 
	 * If the parser isn't defined, the {@link SimpleHttpClientParser} is set.
	 * If the model isn't defined, the {@link MaxDepthModel} is set.
	 * 
	 * @param server
	 *            the starting server of the crawling
	 * @param start
	 *            the starting path of the crawling
	 */
	public final void start(final String server, final String start) {

		// set the default parser
		if (parser == null) {
			LOG.debug("No parser set, defaulting to SimpleHttpClientParser.");
			// parser = new SimpleHttpClientParser();
		}

		// // set default crawler model
		// if (model == null) {
		// LOG.debug("No model set, defaulting to MaxIterationsModel.");
		// model = new MaxIterationsModel();
		// }

		// initialize stop watch
		total.reset();
		loading.reset();
		parsing.reset();
		listener.reset();

		total.start();

		// add at least one link to the list
		model.add(null, server + start);

		// starts the crawling process
		start();

		total.stop();

		// output some statistics
		if (LOG.isInfoEnabled()) {

			Collection visitedURIs = model.getVisitedURIs();
			Collection toVisitURIs = model.getToVisitURIs();

			LOG.info("Visited URIs: " + visitedURIs.size());

			if (!toVisitURIs.isEmpty()) {
				LOG.warn("still URIs to be visited, at least: " + toVisitURIs.size());
			}

			// output stop watch data
			LOG.info("Total time: " + total.getTime() + " ms");
			LOG.info("- loading:  " + loading.getTime() + " ms");
			LOG.info("- parsing:  " + parsing.getTime() + " ms");
			LOG.info("- listener: " + listener.getTime() + " ms");
		}
	}

	/**
	 * Starts the crawling process in a single thread.
	 *
	 * Before starting the crawling process, the model and the parser have to be
	 * set.
	 *
	 * @see com.sjsu.crawler.core.ICrawler#start()
	 */
	public final void start() {
		// loop until there aren't any URIs anymore
		while (!model.isEmpty()) {

			// remove a link from the stack
			Link link = model.pop();

			PageData pageData = parser.load(link);
			if (isPageDataOK(pageData)) {
				final Collection outLinks = parser.parse(pageData, linkFilter);
				// ******To print outlinks/child urls***********
				Iterator iterator = outLinks.iterator();
				while (iterator.hasNext()) {
					String val = (String) iterator.next();
					//System.out.println("Outlinks:"+val);
				}
				fireParserEvent(link, pageData, outLinks);
				// remove already visited URIs from the outgoing links list
				outLinks.removeAll(model.getVisitedURIs());

				// the rest of the URIs are new and can be visited
				model.add(link, outLinks);
			}
		}
	}

}
