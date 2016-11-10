
package com.sjsu.crawler.parser.httpclient;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 * Contains abstract methods for the DownloadHelper and SimpleHttpClientParser.
 *
 
 */
public abstract class AbstractHttpClient {

    /** the HTTP client. */
    protected HttpClient client;

    /**
     * Creates an instance of AbstractHttpClient.
     *
     * @param multiThreaded
     *            true for creating a multi threaded connection manager else
     *            only a single connection is allowed
     */
    protected AbstractHttpClient(boolean multiThreaded) {
        if (multiThreaded) {
            // Creates one instance of HttpClient with a
            // MultiThreadedHttpConnectionManager.
            client = new HttpClient(new MultiThreadedHttpConnectionManager());
        } else {
            // Creates one instance of HttpClient with a single connection
            // manager.
            client = new HttpClient();
        }
    }

    /**
     * Set the proxy settings.
     *
     * @param proxyHost The proxy host
     * @param proxyPort The proxy port
     */
    public void setProxy(String proxyHost, int proxyPort) {
        client.getHostConfiguration().setProxyHost(new ProxyHost(proxyHost, proxyPort));
    }

    /**
     * Returns the proxyHost.
     *
     * @return the proxy host, or <code>null</code> if not set
     */
    public String getProxyHost() {
        return client.getHostConfiguration().getProxyHost();
    }

    /**
     * Returns the proxyPort.
     *
     * @return the proxy port, or <code>-1</code> if not set
     */
    public int getProxyPort() {
        return client.getHostConfiguration().getProxyPort();
    }

    /**
     * Sets the proxy credentials for the given authentication realm. Any
     * previous credentials for the given realm will be overwritten.
     *
     * @param authscope the authentication scope
     * @param credentials the authentication credentials for the given realm.
     *
     * @see org.apache.commons.httpclient.HttpState#setProxyCredentials(AuthScope, Credentials)
     */
    public void setProxyCredentials(AuthScope authscope, Credentials credentials) {
        client.getState().setProxyCredentials(authscope, credentials);
    }

    /**
     * Get the proxy credentials for the given authentication scope.
     *
     * @param authscope {@link AuthScope} authentication scope
     * @return the credentials {@link Credentials}
     *
     * @see org.apache.commons.httpclient.HttpState#getProxyCredentials(AuthScope)
     */
    public Credentials getProxyCredentials(AuthScope authscope) {
        return client.getState().getProxyCredentials(authscope);
    }

    /**
     * Sets the credentials for the given authentication scope. Any previous
     * credentials for the given scope will be overwritten.
     *
     * @param authscope the authentication scope
     * @param credentials the authentication credentials for the given scope.
     *
     * @see org.apache.commons.httpclient.HttpState#setCredentials(AuthScope, Credentials)
     */
    public void setCredentials(AuthScope authscope, Credentials credentials) {
        client.getState().setCredentials(authscope, credentials);
    }

    /**
     * Get the credentials for the given authentication scope.
     *
     * @param authscope the authentication scope
     * @return the credentials
     *
     * @see org.apache.commons.httpclient.HttpState#getCredentials(AuthScope)
     */
    public Credentials getCredentials(AuthScope authscope) {
        return client.getState().getCredentials(authscope);
    }
    
    /** 
     * The {@link HttpConnectionManager} being used to manage
     * connections for this HttpClient.
     * 
     * @return the HTTP connection manager
     * 
     * @see org.apache.commons.httpclient.HttpConnectionManager
     */
    public HttpConnectionManager getHttpConnectionManager() {
        return client.getHttpConnectionManager();
    }

}
