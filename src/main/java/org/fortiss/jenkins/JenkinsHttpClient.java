/**
 * 
 */
package org.fortiss.jenkins;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

/**
 * @author rajat
 *
 */
public class JenkinsHttpClient {

	public static final Logger logger = LogManager
			.getLogger(JenkinsHttpClient.class);
	private URI uri;
	private CloseableHttpClient client;
	private HttpContext localContext;
	private String context;
	private String jenkinsVersion;

	/**
	 * Create an unauthenticated Jenkins HTTP client
	 *
	 * @param uri
	 *            Location of the jenkins server (ex. http://localhost:8080)
	 * @param client
	 *            Configured CloseableHttpClient to be used
	 */
	public JenkinsHttpClient(URI uri, CloseableHttpClient client) {
		this.context = uri.getPath();
		if (!context.endsWith("/")) {
			context += "/";
		}
		this.uri = uri;
		this.client = client;
		this.jenkinsVersion = null;
		logger.debug("uri={}", uri.toString());
	}

	/**
	 * Create an unauthenticated Jenkins HTTP client
	 *
	 * @param uri
	 *            Location of the jenkins server (ex. http://localhost:8080)
	 * @param builder
	 *            Configured HttpClientBuilder to be used
	 */
	public JenkinsHttpClient(URI uri, HttpClientBuilder builder) {
		this(uri, builder.build());
	}

	/**
	 * Create an unauthenticated Jenkins HTTP client
	 *
	 * @param uri
	 *            Location of the jenkins server (ex. http://localhost:8080)
	 */
	public JenkinsHttpClient(URI uri) {
		this(uri, HttpClientBuilder.create());
	}

	/**
	 * Perform a GET request and parse the response and return a simple string
	 * of the content
	 *
	 * @param path
	 *            path to request, can be relative or absolute
	 * @return the entity text
	 * @throws IOException
	 *             in case of an error.
	 * @throws URISyntaxException
	 */
	public String get(String path) throws IOException, URISyntaxException {
		HttpGet getMethod = new HttpGet(URI.create(path));
		HttpResponse response = client.execute(getMethod, localContext);
		getJenkinsVersionFromHeader(response);
		logger.debug("get({}), version={}, responseCode={}", path,
				this.jenkinsVersion, response.getStatusLine().getStatusCode());
		try {
			final InputStream inputStream = response.getEntity().getContent();
			ByteSource byteSource = new ByteSource() {
				@Override
				public InputStream openStream() throws IOException {
					return inputStream;
				}
			};
			return byteSource.asCharSource(Charsets.UTF_8).read();
		} finally {
			EntityUtils.consume(response.getEntity());
			releaseConnection(getMethod);
		}

	}

	/**
	 * Perform a GET request and return the response as InputStream
	 *
	 * @param path
	 *            path to request, can be relative or absolute
	 * @return the response stream
	 * @throws IOException
	 *             in case of an error.
	 */
	public InputStream getFile(URI path) throws IOException {
		HttpGet getMethod = new HttpGet(path);
		HttpResponse response = client.execute(getMethod, localContext);
		getJenkinsVersionFromHeader(response);
		return response.getEntity().getContent();
	}

	private void releaseConnection(HttpRequestBase httpRequestBase) {
		httpRequestBase.releaseConnection();
	}

	protected HttpContext getLocalContext() {
		return localContext;
	}

	/**
	 * @return the version string.
	 */
	public String getJenkinsVersion() {
		return this.jenkinsVersion;
	}

	private void getJenkinsVersionFromHeader(HttpResponse response) {
		Header[] headers = response.getHeaders("X-Jenkins");
		if (headers.length == 1) {
			this.jenkinsVersion = headers[0].getValue();
		}
	}

}
