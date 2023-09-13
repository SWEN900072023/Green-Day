package edu.unimelb.swen90007.mes.scheduled.listener;

import java.util.Date;

import edu.unimelb.swen90007.mes.scheduled.task.EventUpdateTask;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class TaskListener implements ServletContextListener {
    private java.util.Timer timer = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new java.util.Timer(true);
        sce.getServletContext().log("Initializing system scheduled task...");
        timer.schedule(new EventUpdateTask(), new Date(), 24*60*60*1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("system auto task ended.");
        timer.cancel();
    }

}
