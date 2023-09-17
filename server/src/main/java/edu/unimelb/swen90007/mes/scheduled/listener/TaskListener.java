package edu.unimelb.swen90007.mes.scheduled.listener;

import java.sql.SQLException;
import java.util.Date;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Administrator;
import edu.unimelb.swen90007.mes.scheduled.task.EventUpdateTask;
import edu.unimelb.swen90007.mes.service.IAppUserService;
import edu.unimelb.swen90007.mes.service.impl.AppUserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskListener implements ServletContextListener {
    private java.util.Timer timer = null;

    private static final Logger logger = LogManager.getLogger(TaskListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        IAppUserService appUserService = new AppUserService();
        Administrator admin = new Administrator(
                0, "root@test.io", "123456", "Root", "Admin");
        try {
            appUserService.register(admin);
        } catch (UserAlreadyExistsException e) {
            logger.info("Root admin initialization: root admin already exists.");
        } catch (SQLException e) {
            logger.error("Failed to initialize the Root Admin.");
        }

        timer = new java.util.Timer(true);
        logger.info("Initializing system scheduled task...");
        timer.schedule(new EventUpdateTask(), new Date(), 24*60*60*1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("system auto task ended.");
        timer.cancel();
    }

}
