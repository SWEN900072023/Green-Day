package edu.unimelb.swen90007.mes.servlet;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.Administrator;
import edu.unimelb.swen90007.mes.service.IAppUserService;
import edu.unimelb.swen90007.mes.service.impl.AppUserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

@WebListener
public class ApplicationListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(ApplicationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        IAppUserService userService = new AppUserService();
        Administrator admin = new Administrator(
                0, "root@test.io", "123456", "Root", "Admin");
        try {
            userService.register(admin);
        } catch (UserAlreadyExistsException e) {
            logger.info("Root admin initialization: root admin already exists.");
        } catch (SQLException e) {
            logger.error("Failed to initialize the Root Admin.");
        }
    }

}
