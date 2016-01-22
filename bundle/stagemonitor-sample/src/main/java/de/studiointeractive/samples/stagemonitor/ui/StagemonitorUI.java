package de.studiointeractive.samples.stagemonitor.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.apache.felix.ipojo.annotations.Provides;

/**
 * vaadin-osgi-stagemonitor
 * de.studiointeractive.samples.stagemonitor.ui
 */
@org.apache.felix.ipojo.annotations.Component
@Provides(specifications = {UI.class})
public class StagemonitorUI extends UI {
	@Override
	protected void init(VaadinRequest vaadinRequest) {

		setContent(new Label("Hello Stagemonitor!"));

	}
}
