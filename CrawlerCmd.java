
package com.sjsu.crawler.cmd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import com.sjsu.crawler.Crawler;
import com.sjsu.crawler.core.ICrawler;
import com.sjsu.crawler.database.DB;
import com.sjsu.crawler.filter.ILinkFilter;
import com.sjsu.crawler.filter.ServerFilter;
import com.sjsu.crawler.link.Link;
import com.sjsu.crawler.model.MaxDepthModel;
import com.sjsu.crawler.parser.httpclient.JsoupClientParser;
import com.sjsu.crawler.util.StringUtil;

/**
 * Project:WebCrawler
 */
public class CrawlerCmd {

	/*
	 * java -cp
	 * lib/core/commons-codec-1.3.jar;lib/core/commons-httpclient-3.1.jar;lib/
	 * core/commons-logging-1.1.1.jar;dist/crawler-1.3.0-dev-16May09.jar
	 * com.sjsu.crawler.cmd.CrawlerCmd -iterations 1 http://www.spiegel.de
	 * 
	 * java -cp
	 * lib/core/commons-codec-1.3.jar;lib/core/commons-httpclient-3.1.jar;lib/
	 * core/commons-logging-1.1.1.jar;dist/crawler-1.3.0-dev-16May09.jar
	 * com.sjsu.crawler.cmd.CrawlerCmd -filterserver http://www.heise.de
	 * -iterations 3 http://www.heise.de
	 * 
	 * java -cp
	 * lib/core/commons-codec-1.3.jar;lib/core/commons-httpclient-3.1.jar;lib/
	 * core/commons-logging-1.1.1.jar;dist/crawler-1.3.0-dev-16May09.jar
	 * com.sjsu.crawler.cmd.CrawlerCmd -filterserver http://www.spiegel.de
	 * -iterations 1 http://www.spiegel.de > links.txt type links.txt
	 */

	private static final String USAGE = "Usage: java " + "-cp commons-codec-1.3.jar" + File.pathSeparatorChar
			+ "commons-httpclient-3.1.jar" + File.pathSeparatorChar + "commons-logging-1.1.1.jar"
			+ File.pathSeparatorChar + "crawler-1.3.0.jar " + CrawlerCmd.class.getName() + " "
			+ "[-depth <max depth>] [-<filterserver> <server>] [-<httpproxy> <hostname:port>] " + "<URL>";

	/**
	 * @param args
	 *            arguments of the command line
	 */
	public static void main(String[] args) throws IOException, SQLException {

		int depth = MaxDepthModel.DEFAULT_MAX_DEPTH;
		// int iterations = MaxDepthModel.DEFAULT_MAX_ITERATIONS;
		String uri = null;
		ILinkFilter linkFilter = null;
		String proxyHost = null;
		int proxyPort = -1;
		final DB db = new DB();
		String sql = "TRUNCATE TABLE Crawler_Record";
		db.runSql2(sql);
		String sql1 = "TRUNCATE TABLE ToVisitURIs";
		db.runSql2(sql1);

		// To delete existing files in the directory
		File delfile = new File("/Users/milcegeorge/Documents/Crawler_Output/DataFiles");
		String[] myFiles;
		if (delfile.isDirectory()) {
			myFiles = delfile.list();
			for (int i = 0; i < myFiles.length; i++) {
				File myFile = new File(delfile, myFiles[i]);
				myFile.delete();
			}
		}
		// File delfile1 = new File("ParserOut");
		File delfile1 = new File("/Users/milcegeorge/Documents/Crwaler/ParserOut");
		String[] myFiles1;
		if (delfile1.isDirectory()) {
			myFiles1 = delfile1.list();
			for (int i = 0; i < myFiles1.length; i++) {
				File myFile = new File(delfile1, myFiles1[i]);
				myFile.delete();
			}
		}

		// get command line parameters
		for (int i = 0; i < args.length; i++) {
			if (i == args.length - 1) { // get uri
				uri = args[i];
			} else if ("-depth".equals(args[i])) { // parse -depth option
				depth = Integer.parseInt(args[++i]);
			}
			// else if ("-iterations".equals(args[i])) { // parse -iterations
			// option
			// iterations = Integer.parseInt(args[++i]);}
			else if ("-filterserver".equals(args[i])) { // parse -filterserver
														// option
				linkFilter = new ServerFilter(args[++i]);
			} else if ("-httpproxy".equals(args[i])) { // parse -httpproxy
														// option
				final String httpproxy = args[++i];
				final int colon = httpproxy.indexOf(':');
				if (colon >= 0) {
					proxyHost = httpproxy.substring(0, colon);
					proxyPort = Integer.parseInt(httpproxy.substring(colon + 1));
				} else {
					proxyHost = httpproxy;
				}
			} else {
				System.err.println(USAGE);
				System.exit(1);
			}
		}

		if (!StringUtil.hasLength(uri)) {
			System.err.println(USAGE);
			System.exit(1);
		}

		// Call to JsoupClientParser
		final JsoupClientParser parser = new JsoupClientParser();
		if (proxyHost != null) {
			parser.setProxy(proxyHost, proxyPort);
		}

		// TODO support MultiThreadedCrawler later
		final ICrawler crawler = new Crawler();
		crawler.setParser(parser);
		// crawler.setModel(new MaxDepthModel(depth, iterations));
		crawler.setModel(new MaxDepthModel(depth));
		crawler.setLinkFilter(linkFilter);
		crawler.getModel().add(null, uri);
		crawler.start();

		System.out.println("----------------VISITED------------");
		// Show visited links
		Iterator vList = crawler.getModel().getVisitedURIs().iterator();
		while (vList.hasNext()) {
			final Link link = (Link) vList.next();
			// System.out.println("Visited:" + link.getURI());
		}

		System.out.println("----------------TO VISIT------------");
		// Show not visited links
		Iterator nvList = crawler.getModel().getToVisitURIs().iterator();

		while (nvList.hasNext()) {
			final Link link = (Link) nvList.next();
			// System.out.println("To Visit:" + link.getURI());
		}

		String rootpath = "C://Users/milcegeorge/Documents/Crwaler/parserout";
		String filepaths;
		File folder = new File(rootpath);
		File[] listOfFiles = folder.listFiles();

		java.io.File file1 = new java.io.File("/Users/milcegeorge/Documents/Crwaler/parserout/filelist.txt");
		if (!file1.exists()) {
			file1.createNewFile();
		}

		FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
		BufferedWriter bw1 = new BufferedWriter(fw1);

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				filepaths = listOfFiles[i].getAbsolutePath();
				bw1.write(filepaths);
				bw1.newLine();
				// bw1.flush();

			}
		}
		bw1.close();

	}

}
