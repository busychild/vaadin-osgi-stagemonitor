package de.studiointeractive.samples.stagemonitor.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import de.studiointeractive.samples.stagemonitor.ui.StagemonitorUI;
import org.stagemonitor.core.Stagemonitor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * vaadin-osgi-stagemonitor
 * de.studiointeractive.samples.stagemonitor.servlet
 */
@WebServlet(asyncSupported = true)
@VaadinServletConfiguration(ui = StagemonitorUI.class, heartbeatInterval = 10, productionMode = true)
public class StagemonitorServlet extends VaadinServlet {

	private final UIProvider provider;

	public StagemonitorServlet(UIProvider provider) {
		this.provider = provider;
	}

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();

		Stagemonitor.init();

		// This doesn't work :(
		//new WebPlugin().onStartup(null, getServletContext());
	}

}
