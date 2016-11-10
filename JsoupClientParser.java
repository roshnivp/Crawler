
package com.sjsu.crawler.parser.httpclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import com.sun.java.util.jar.pack.Package.File;
import com.sjsu.crawler.database.DB;
import com.sjsu.crawler.filter.ILinkFilter;
import com.sjsu.crawler.link.Link;
import com.sjsu.crawler.parser.IParser;
import com.sjsu.crawler.parser.PageData;
import com.sjsu.crawler.parsergrammar.MyNewGrammar;

public class JsoupClientParser extends AbstractHttpClient implements IParser {

	private static final transient Log LOG = LogFactory.getLog(JsoupClientParser.class);

	/**
	 * user agent HTTP header of the crawler.
	 **/
	public static final String USER_AGENT = "WebCrawler";

	/**
	 * The constructor of JsoupClientParser
	 */
	public JsoupClientParser() {
		this(false);
	}

	/**
	 * Creates an instance of JsoupClientParser.
	 *
	 * @param multiThreaded
	 *            true for creating a multi threaded connection manager else
	 *            only a single connection is allowed
	 */
	public JsoupClientParser(boolean multiThreaded) {
		super(multiThreaded);
	}

	/**
	 * Loads the data of the URI. A crawler can load different URIs at the same
	 * time and parse them lately. Hence all necessary information have to be
	 * stored in a PageData object. E.g. different threads can download the
	 * content of the URI parallel and parse them in a different order.
	 *
	 * @param link
	 *            the link of the page
	 * @return the page data of the uri with a status code
	 *
	 * 
	 */
	public PageData load(Link link) {

		PageDataHttpClient pageDataHttpClient = null;
		pageDataHttpClient = new PageDataHttpClient(link, PageData.OK);
		return pageDataHttpClient;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.sjsu.crawler.parser.IParser#parse(com.sjsu.crawler.parser.PageData,
	 *      com.sjsu.crawler.filter.ILinkFilter)
	 */
	public Collection parse(PageData pageData, ILinkFilter linkFilter) {
		Collection result = new HashSet();
		// System.out.println(result);
		try {

			if (pageData.getLink().getURI() != null && !pageData.getLink().getURI().isEmpty()
					&& ((!pageData.getLink().getURI().endsWith(".jpg"))
							|| (!pageData.getLink().getURI().endsWith(".png"))
							|| (!pageData.getLink().getURI().endsWith(".gif"))))

			{
				System.out.println("\nParsing url " + pageData.getLink().getURI());
				String parseurl = pageData.getLink().getURI();
				String parseurl1 = removeLastSlash(parseurl);

				// Delete matching urls from Crawler_Record table in database
				String sql1 = "DELETE FROM Crawler_Record where URL=?";
				PreparedStatement stmnt1 = DB.conn.prepareStatement(sql1);
				stmnt1.setString(1, parseurl1);
				stmnt1.execute();

				// Insert new records in Crawler_Record table in database
				String sql2 = "INSERT INTO Crawler_Record" + "(URL) values" + "(?);";
				PreparedStatement stmnt = DB.conn.prepareStatement(sql2);
				stmnt.setString(1, parseurl1);
				stmnt.execute();

				// Connecting url to Jsoup parser
				long jstartTime = new Date().getTime();
				Document doc = Jsoup.connect(pageData.getLink().getURI()).followRedirects(true)
						.userAgent(
								"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36")
						.referrer("http://www.google.com").timeout(10 * 1000).get();
				String urlink = pageData.getLink().getURI();

				// System.out.println("Source code of parsered
				// url:"+doc.toString());

				/**
				 * Change the file location
				 * 
				 */

				// Write output files into location specified
//				java.io.File file = new java.io.File("/Users/milcegeorge/Documents/Crawler_Output/DataFiles/data-"
//						+ pageData.getLink().getURI().hashCode() + ".txt");
//				// if file doesn't exists, then create it
//				// --String urlink1=pageData.getLink().getURI();
//				if (!file.exists()) {
//					file.createNewFile();
//				}
//
//				FileWriter fw = new FileWriter(file.getAbsoluteFile());
//				BufferedWriter bw = new BufferedWriter(fw);
//				bw.write(doc.toString());
//				bw.close();

				MyNewGrammar.grammar( urlink);

				Elements linksOnPage = doc.select("a[href]");// Get the
																// sub-links of
																// currently
																// parsing url
				for (Element link : linksOnPage) {

					// if ((link.attr("abs:href").contains("sjsu.edu") &&
					// (link.attr("abs:href").startsWith("http"))))
					result.add(link.attr("abs:href"));

				}
				int i=0;
				long jendTime = new Date().getTime();
				long jTime = (jendTime - jstartTime);
				System.out.println("Time in sec:" + jTime);

				// String sql4 = "INSERT INTO ToVisitURIs" + "(TV_URL
				// ,TimeinSec) values" + "(?,?);";
				// PreparedStatement stmnt2 = DB.conn.prepareStatement(sql4);
				// stmnt2.setString(1, parseurl1);
				// stmnt2.setLong(2,jTime);
				// stmnt2.execute();

			}
		} catch (IOException e) {
			// System.out.println("Error while connecting to uri " +
			// pageData.getLink().getURI());
			((Throwable) e).printStackTrace();
		} catch (Throwable s) {
			// System.out.println("Error while connecting to uri " +
			// pageData.getLink().getURI());
			((Throwable) s).printStackTrace();
		}

		return result;
	}

	private void pgrammar(String fname) {
		// TODO Auto-generated method stub

	}

	// Remove urls ends with "/"
	String removeLastSlash(String url) {
		if (url.endsWith("/")) {
			return url.substring(0, url.lastIndexOf("/"));
		} else {
			return url;
		}
	}

}
