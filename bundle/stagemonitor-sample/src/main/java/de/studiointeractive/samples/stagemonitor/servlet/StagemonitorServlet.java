package de.studiointeractive.samples.stagemonitor.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import de.studiointeractive.samples.stagemonitor.ui.StagemonitorUI;
import org.stagemonitor.core.Stagemonitor;
import org.stagemonitor.core.configuration.source.PropertyFileConfigurationSource;
import org.stagemonitor.web.WebPlugin;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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

		getServletContext().addListener(new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				getServletContext().addServlet(TestServlet.class.getSimpleName(), new TestServlet()).addMapping("/test");
				try {
					new WebPlugin().onStartup(null, getServletContext());
				} catch (ServletException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {

			}
		});


		super.servletInitialized();




		Stagemonitor.init();




	}



	@Override
	public ServletContext getServletContext() {
		ServletContext context = super.getServletContext();
		return context;
	}
}
