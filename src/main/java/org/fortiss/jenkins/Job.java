/**
 * 
 */
package org.fortiss.jenkins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * @author rajat
 *
 */

public class Job {

	private String name;
	private String url;
	protected JenkinsHttpClient client;

	public Job() {

	}

	public Job(String name, String url) {
		this();
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * Get a file from workspace.
	 * 
	 * @param fileName
	 *            The name of the file to download from workspace. You can also
	 *            access files which are in sub folders of the workspace.
	 * @return The string which contains the content of the file.
	 * @throws IOException
	 *             in case of an error.
	 */
	public String getFileFromWorkspace(String fileName) throws IOException {
		InputStream is = client.getFile(URI.create(url + "/ws/" + fileName));
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString("UTF-8");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Job job = (Job) o;

		if (name != null ? !name.equals(job.name) : job.name != null)
			return false;
		if (url != null ? !url.equals(job.url) : job.url != null)
			return false;

		return true;
	}

}