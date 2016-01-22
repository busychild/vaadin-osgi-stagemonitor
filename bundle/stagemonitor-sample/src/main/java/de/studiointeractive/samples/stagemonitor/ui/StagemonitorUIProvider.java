package de.studiointeractive.samples.stagemonitor.ui;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import de.studiointeractive.samples.stagemonitor.api.ApplicationConstants;
import org.apache.felix.ipojo.*;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * vaadin-osgi-stagemonitor
 * de.studiointeractive.samples.stagemonitor.ui
 */
@Component
@Provides(specifications = {UIProvider.class})
public class StagemonitorUIProvider extends UIProvider {


	private String applicationId;
	private String applicationName;
	private String applicationAlias;
	private List<String> domains;
	private List<ComponentInstance> uis;
	private int instanceCount;

	@Requires(from = "de.studiointeractive.samples.stagemonitor.ui.StagemonitorUI")
	private Factory factory;

	public StagemonitorUIProvider() {
		super();

		uis = new ArrayList<>();
		instanceCount = 0;
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent uiClassSelectionEvent) {
		return StagemonitorUI.class;
	}

	@Override
	public UI createInstance(UICreateEvent event) {

		StagemonitorUI ui = new StagemonitorUI();
		String uiID = getApplicationId() + "-" + getInstanceCount();

		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(ApplicationConstants.INSTANCE_OBJECT_PROPERTY_KEY, ui);

		try {

			ComponentInstance componentInstance = factory.createComponentInstance(props);

			getUis().add(componentInstance);
			setInstanceCount(getInstanceCount() + 1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ui;
	}

	@Invalidate
	private void invalidate() {
		for (ComponentInstance instance : getUis()) {
			stop(instance);
		}
	}

	public void stop(ComponentInstance instance) {
		if (instance != null && uis.contains(instance)) {
			instance.dispose();
			uis.remove(instance);
		}
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setApplicationAlias(String applicationAlias) {
		this.applicationAlias = applicationAlias;
	}

	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	public int getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}

	public List<ComponentInstance> getUis() {
		return uis;
	}

	public void setUis(List<ComponentInstance> uis) {
		this.uis = uis;
	}
}
