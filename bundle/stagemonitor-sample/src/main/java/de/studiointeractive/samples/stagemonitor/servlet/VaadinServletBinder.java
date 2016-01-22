package de.studiointeractive.samples.stagemonitor.servlet;

import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import de.studiointeractive.samples.stagemonitor.StagemonitorApplication;
import de.studiointeractive.samples.stagemonitor.api.ApplicationConstants;
import de.studiointeractive.samples.stagemonitor.resource.ResourceProvider;
import de.studiointeractive.samples.stagemonitor.ui.api.UIProviderFactory;
import org.apache.felix.ipojo.annotations.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;
import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component(name = VaadinServletBinder.COMPONENT_NAME)
@Instantiate
public class VaadinServletBinder implements BundleListener {

	public static final String COMPONENT_NAME = "VaadinServletBinder";

	private final BundleContext bundleContext;

	@Requires
	private HttpService httpService;
	private Map<String, StagemonitorApplication> applications = new ConcurrentHashMap<String, StagemonitorApplication>();

	public VaadinServletBinder(@Context BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Validate
	protected void setUp() {
		bundleContext.addBundleListener(this);
	}

	/**
	 * Binds the vaadin applications
	 *
	 * @param application
	 * @param properties
	 */
	@Bind(aggregate = true, optional = true)
	public void bindVaadinApplication(StagemonitorApplication application, Dictionary properties) {

		UIProviderFactory uiProviderFactory = application.getUIProviderFactory();
		UIProvider uiProvider = uiProviderFactory.createUIProvider(properties);

		String applicationAlias = (String) properties.get(ApplicationConstants.APPLICATION_ALIAS_PROPERTY_KEY);
		if (applicationAlias != null && applications.containsKey(applicationAlias)) {
			return;
		}

		//UIProvider uiProvider = uiProviderFactory.createUIProvider(properties);
		VaadinServlet servlet = application.createServlet(uiProvider);
		ResourceProvider resourceProvider = application.getResourceProvider();

		try {
			httpService.registerServlet(applicationAlias, servlet, null, resourceProvider);
			applications.put(applicationAlias, application);
		} catch (ServletException | NamespaceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unbinds the vaadin applications
	 *
	 * @param application
	 * @param properties
	 */
	@Unbind
	public void unbindVaadinApplication(StagemonitorApplication application, Dictionary properties) {
		
		UIProviderFactory uiProviderFactory = application.getUIProviderFactory();
		uiProviderFactory.stopUIProvider(properties);
		
		String applicationAlias = (String) properties.get(ApplicationConstants.APPLICATION_ALIAS_PROPERTY_KEY);

		httpService.unregister(applicationAlias);

		if (applications.containsKey(applicationAlias)) {
			applications.remove(applicationAlias);
		}
	}

	@Override
	public void bundleChanged(BundleEvent event) {

	}
}
