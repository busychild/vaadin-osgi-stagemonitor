package de.studiointeractive.samples.stagemonitor.ui;

import com.vaadin.server.UIProvider;
import de.studiointeractive.samples.stagemonitor.StagemonitorApplication;
import de.studiointeractive.samples.stagemonitor.api.ApplicationConstants;
import de.studiointeractive.samples.stagemonitor.ui.api.UIProviderFactory;
import org.apache.felix.ipojo.*;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * vaadin-osgi-stagemonitor
 * de.studiointeractive.samples.stagemonitor.ui
 */
@Component(name=StagemonitorUIProviderFactory.COMPONENT_NAME)
@Instantiate
@Provides
public class StagemonitorUIProviderFactory implements UIProviderFactory {

	public static final String COMPONENT_NAME = "StagemonitorUIProviderFactory";

	@Requires(from = "de.studiointeractive.samples.stagemonitor.ui.StagemonitorUIProvider")
	private Factory stageMonitorUIProviderFactory;

	private Map<String, ComponentInstance> providers;

	public StagemonitorUIProviderFactory() {
		providers = new ConcurrentHashMap<String, ComponentInstance>();
	}

	@Override
	public UIProvider createUIProvider(Dictionary properties) {
		String instanceName = (String) properties.get(ApplicationConstants.INSTANCE_NAME_PROPERTY_KEY);
		String applicationName = (String) properties.get(ApplicationConstants.APPLICATION_NAME_PROPERTY_KEY);
		String applicationAlias = (String) properties.get(ApplicationConstants.APPLICATION_ALIAS_PROPERTY_KEY);
		String[] applicationDomains = (String[]) properties.get(ApplicationConstants.APPLICATION_DOMAINS_PROPERTY_KEY);

		StagemonitorUIProvider provider = new StagemonitorUIProvider();

		provider.setApplicationId(instanceName);
		provider.setApplicationName(applicationName);
		provider.setApplicationAlias(applicationAlias);
		provider.setDomains(Arrays.asList(applicationDomains));

		Properties instanceProperties = new Properties();
		instanceProperties.put(ApplicationConstants.INSTANCE_OBJECT_PROPERTY_KEY, provider);
		ComponentInstance componentInstance;
		try {
			componentInstance = stageMonitorUIProviderFactory.createComponentInstance(instanceProperties);
			providers.put(instanceName, componentInstance);
		} catch (UnacceptableConfiguration e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return provider;
	}

	@Override
	public void stopUIProvider(Dictionary properties) {
		String instanceName = (String) properties.get(ApplicationConstants.INSTANCE_NAME_PROPERTY_KEY);
		ComponentInstance componentInstance = providers.get(instanceName);
		if (componentInstance != null) {
			componentInstance.stop();
			componentInstance.dispose();
			providers.remove(instanceName);
		}
	}

	@Override
	public String getName() {
		return StagemonitorApplication.COMPONENT_NAME;
	}
}
