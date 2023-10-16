package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.model.Venue;
import edu.unimelb.swen90007.mes.service.impl.AdminService;

import java.sql.SQLException;

public class Admin extends Thread {
    public static void createVenue() {
        AdminService adminService = new AdminService();
        // Create Venues
        Venue venue1 = new Venue("Name1", "Address1", 1000);
        Venue venue2 = new Venue("Name2", "Address2", 1000);
        adminService.createVenue(venue1);
        adminService.createVenue(venue2);
    }

    public static void viewUsers() {
        AdminService adminService = new AdminService();
        // View Users
        try{
            adminService.viewAllCustomers();
            adminService.viewAllEventPlanners();
            adminService.viewAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
