package com.city.web;

import com.city.service.SensorNetwork;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SensorBootstrapListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Starts sensor telemetry worker threads and database snapshot flusher upon servlet context startup
        SensorNetwork network = new SensorNetwork();
        network.startAll();
        sce.getServletContext().setAttribute("sensorNetwork", network);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanly terminates executor thread pools when Tomcat redeploys or stops the application
        SensorNetwork network = (SensorNetwork) sce.getServletContext().getAttribute("sensorNetwork");
        if (network != null) {
            network.stopAll();
        }
    }
}
