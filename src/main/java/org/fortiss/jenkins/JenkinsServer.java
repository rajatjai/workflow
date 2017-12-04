/**
 * 
 */
package org.fortiss.jenkins;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author rajat
 *
 */
public class JenkinsServer {
	public static final Logger logger = LogManager
			.getLogger(JenkinsServer.class);
	private final JenkinsHttpClient client;

	/**
	 * Create a new Jenkins server reference given only the server address
	 *
	 * @param serverUri
	 *            address of jenkins server (ex. http://localhost:8080/jenkins)
	 */
	public JenkinsServer(URI serverUri) {
		this(new JenkinsHttpClient(serverUri));
	}

	/**
	 * Create a new Jenkins server directly from an HTTP client (ADVANCED)
	 *
	 * @param client
	 *            Specialized client to use.
	 */
	public JenkinsServer(JenkinsHttpClient client) {
		this.client = client;
	}

	/**
	 * Get the current status of the Jenkins end-point by pinging it.
	 *
	 * @return true if Jenkins is up and running, false otherwise
	 */
	public boolean isRunning() {
		try {
			client.get("/");
			return true;
		} catch (IOException e) {
			logger.debug("isRunning()", e);
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @return {@link JenkinsVersion}
	 */
	public String getVersion() {
		return client.getJenkinsVersion();
	}

}
