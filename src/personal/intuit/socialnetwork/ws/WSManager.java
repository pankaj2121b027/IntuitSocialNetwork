package personal.intuit.socialnetwork.ws;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import personal.intuit.socialnetwork.application.Application;

public class WSManager implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
    {
		try {
			Application.init();
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize application" , e);
		}
		
    }


	@Override
    public void contextDestroyed(ServletContextEvent arg0) 
    {
		Application.close();     
    }
}
