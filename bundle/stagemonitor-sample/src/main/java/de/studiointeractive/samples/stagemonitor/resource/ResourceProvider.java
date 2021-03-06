package de.studiointeractive.samples.stagemonitor.resource;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * vaadin-osgi-stagemonitor
 * de.studiointeractive.samples.stagemonitor.resource
 */
public class ResourceProvider implements HttpContext {

	/**
	 * The list of all bundles
	 */
	private List<Bundle> resources = new ArrayList<Bundle>();

	@Override
	public URL getResource(String uri) {
		URL resource = null;
		// iterate over the vaadin bundles and try to find the requested resource
		for (Bundle bundle : resources) {
			resource = bundle.getResource(uri);
			if (resource != null) {
				break;
			}
		}
		return resource;
	}

	/**
	 * Adds a bundle that may potentially contain a requested resource.
	 *
	 * @param bundle
	 */
	public void add(Bundle bundle) {
		if (!resources.contains(bundle)) {
			resources.add(bundle);
		}
	}

	/**
	 * Removes a bundle that may potentially contain a requested resource.
	 *
	 * @param bundle
	 */
	public void remove(Bundle bundle) {
		resources.remove(bundle);
	}

	@Override
	public String getMimeType(String arg0) {
		if (arg0.endsWith(".jpg")) {
			return "image/jpeg";
		} else if (arg0.endsWith(".png")) {
			return "image/png";
		} else if (arg0.endsWith(".mp4")) {
			return "video/mp4";
		} else if (arg0.endsWith(".pdf")) {
			return "application/pdf";
		} else {
			return null;
		}
	}

	@Override
	public boolean handleSecurity(HttpServletRequest request,
								  HttpServletResponse response) throws IOException {
		return true;
	}
}
