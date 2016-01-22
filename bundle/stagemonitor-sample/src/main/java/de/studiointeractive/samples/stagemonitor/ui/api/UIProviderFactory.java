package de.studiointeractive.samples.stagemonitor.ui.api;

import com.vaadin.server.UIProvider;

import java.util.Dictionary;

/**
 * vaadin-osgi-stagemonitor
 * de.studiointeractive.samples.stagemonitor.ui.api
 */
public interface UIProviderFactory {

	public abstract UIProvider createUIProvider(Dictionary properties);

	public abstract void stopUIProvider(Dictionary properties);

	public abstract String getName();
}
