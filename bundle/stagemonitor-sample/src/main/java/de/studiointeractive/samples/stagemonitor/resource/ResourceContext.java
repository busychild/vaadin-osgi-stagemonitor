package de.studiointeractive.samples.stagemonitor.resource;

import de.studiointeractive.samples.stagemonitor.api.ApplicationConstants;
import org.apache.felix.ipojo.annotations.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;

/**
 * vaadin-osgi-stagemonitor
 * de.studiointeractive.samples.stagemonitor.resource
 */
@org.apache.felix.ipojo.annotations.Component(name = ResourceContext.COMPONENT_NAME)
@Instantiate
@Provides(specifications = ResourceContext.class, properties = {
		@org.apache.felix.ipojo.annotations.StaticServiceProperty(name = ApplicationConstants.RESOURCE_CONTEXT_NAME_PROPERTY_KEY, type = "java.lang.String", value = "VAADIN"),
		@org.apache.felix.ipojo.annotations.StaticServiceProperty(name = ApplicationConstants.RESOURCE_CONTEXT_ALIAS_PROPERTY_KEY, type = "java.lang.String", value = "/VAADIN")})
public class ResourceContext implements BundleListener {

	public static final String COMPONENT_NAME = "VaadinResourceProvider";
	private final BundleContext bundleContext;
	private ResourceProvider resourceProvider;
	private HttpService httpService;

	public ResourceContext(@Context BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Called by OSGi DS if the component is activated.
	 */
	@Validate
	protected void start() {
		handleStartedBundles(bundleContext);
		bundleContext.addBundleListener(this);
	}

	/**
	 * Returns the resource provider to access static resources.
	 *
	 * @return
	 */
	private ResourceProvider getResourceProvider() {
		if (resourceProvider == null) {
			resourceProvider = new ResourceProvider();
		}
		return resourceProvider;
	}

	/**
	 * Called by OSGi DS if the component is deactivated.
	 */
	@Invalidate
	protected void stop() {
		bundleContext.removeBundleListener(this);
		resourceProvider = null;
	}

	/**
	 * Binds the http service to this component. Called by OSGi-DS.
	 *
	 * @param service
	 */
	@Bind
	protected void bindHttpService(HttpService service) {
		httpService = service;
		try {
			// register the servlet at the http service
			httpService.registerServlet("/VAADIN", new ResourceProviderServlet(), null, getResourceProvider());

		} catch (ServletException e) {
			e.printStackTrace();
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unbinds the http service from this component. Called by OSGi-DS.
	 *
	 * @param service
	 */
	@Unbind
	protected void unbindHttpService(HttpService service) {
		// unregister the servlet from the http service
		httpService.unregister("/VAADIN");
	}

	/**
	 * Tries to find proper started bundles and adds them to resource provider.
	 * Since bundle changed listener will not find them.
	 */
	protected void handleStartedBundles(BundleContext bundleContext) {
		for (Bundle bundle : bundleContext.getBundles()) {
			if (bundle.getState() == Bundle.ACTIVE
					&& bundleShouldBeRegistered(bundle)) {
				getResourceProvider().add(bundle);
			}
		}
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		if (bundleShouldBeRegistered(event.getBundle())) {
			if (event.getType() == BundleEvent.STARTED) {
				getResourceProvider().add(event.getBundle());
			} else if (event.getType() == BundleEvent.STOPPED) {
				getResourceProvider().remove(event.getBundle());
			}
		}
	}

	private boolean bundleShouldBeRegistered(Bundle bundle) {

		String name = bundle.getSymbolicName();
		Boolean isValid = false;

		if (name.contains("stagemonitor") || name.startsWith("com.vaadin")) {
			isValid = true;
		}

		return isValid;
	}

	public HttpContext getHttpContext() {
		return getResourceProvider();
	}
}
