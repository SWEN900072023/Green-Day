package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.model.Venue;
import edu.unimelb.swen90007.mes.service.impl.AdminService;

import java.sql.SQLException;

public class Admin {
    /**
     * Simulate the administrator to create two venues.
     */
    public static void createVenue() {
        AdminService adminService = new AdminService();
        Venue venue1 = new Venue("Mock Venue 1", "Mock Venue 1 Address", 1000);
        Venue venue2 = new Venue("Mock Venue 2", "Mock Venue 2 Address", 1000);
        adminService.createVenue(venue1);
        adminService.createVenue(venue2);
    }

    /**
     * Simulate the administrator to view all other users.
     */
    public static void viewUsers() {
        AdminService adminService = new AdminService();
        try {
            adminService.viewAllCustomers();
            adminService.viewAllEventPlanners();
            adminService.viewAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
