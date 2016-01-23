package de.studiointeractive.samples.stagemonitor;

import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import de.studiointeractive.samples.stagemonitor.api.ApplicationConstants;
import de.studiointeractive.samples.stagemonitor.resource.ResourceProvider;
import de.studiointeractive.samples.stagemonitor.servlet.StagemonitorServlet;
import de.studiointeractive.samples.stagemonitor.ui.StagemonitorUIProviderFactory;
import de.studiointeractive.samples.stagemonitor.ui.api.UIProviderFactory;
import org.apache.felix.ipojo.annotations.*;
import org.ops4j.pax.web.service.WebContainer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * vaadin-osgi-stagemonitor
 * java.de.studiointeractive.samples.stagemonitor
 */
@Component
@Instantiate
@Provides(specifications = StagemonitorApplication.class, properties = {
		@org.apache.felix.ipojo.annotations.StaticServiceProperty(name = ApplicationConstants.APPLICATION_NAME_PROPERTY_KEY, type = "java.lang.String", value = "stagemonitor-application"),
		@org.apache.felix.ipojo.annotations.StaticServiceProperty(name = ApplicationConstants.APPLICATION_ALIAS_PROPERTY_KEY, type = "java.lang.String", value = "/sampleapp"),
		@org.apache.felix.ipojo.annotations.StaticServiceProperty(name = ApplicationConstants.APPLICATION_DOMAINS_PROPERTY_KEY, type = "java.lang.String[]", value = "sampleapp")})
public class StagemonitorApplication {

	public static final String COMPONENT_NAME = "StagemonitorApplication";

	private final BundleContext bundleContext;

	@Requires(filter = "(factory.name=" + StagemonitorUIProviderFactory.COMPONENT_NAME + ")")
	private UIProviderFactory uiProviderFactory;

	private de.studiointeractive.samples.stagemonitor.ui.api.UIProviderFactory UIProviderFactory;
	private ResourceProvider resourceProvider;

	public StagemonitorApplication(@Context BundleContext bundleContext) {
		this.bundleContext = bundleContext;


		ServiceReference serviceReference = bundleContext.getServiceReference( "org.ops4j.pax.web.service.WebContainer" );

		WebContainer webContainer = (WebContainer) bundleContext.getService(serviceReference);

		System.out.println("GSD");
	}

	public UIProviderFactory getUIProviderFactory() {
		return UIProviderFactory;
	}

	public VaadinServlet createServlet(UIProvider uiProvider) {
		return new StagemonitorServlet(uiProvider);
	}

	public ResourceProvider getResourceProvider() {
		if (resourceProvider == null) {
			resourceProvider = new ResourceProvider();
			resourceProvider.add(bundleContext.getBundle());
		}
		return resourceProvider;
	}
}
