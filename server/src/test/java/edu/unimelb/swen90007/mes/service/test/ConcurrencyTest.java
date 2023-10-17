package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.model.Venue;
import edu.unimelb.swen90007.mes.service.impl.AdminService;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the concurrency test.
 */
public class ConcurrencyTest {
    /**
     * Simulate the administrator to create two venues.
     */
    public static void createVenue() {
        AdminService adminService = new AdminService();
        Venue venue1 = new Venue("Mock Venue 1", "Mock Venue Address 1", 100);
        Venue venue2 = new Venue("Mock Venue 2", "Mock Venue Address 2", 100);
        adminService.createVenue(venue1);
        adminService.createVenue(venue2);
    }

    public static void main(String[] args) {
        System.out.println("Concurrency Testing Started");

        // Simulate the administrator to create two venues.
        createVenue();

        // Create four event planners.
        // The first two event planners create events,
        // whereas the last two modify the music events created by the first two.
        CreateEventThread ep1 = new CreateEventThread
                ("ep1@gmail.com", "ep1@gmail.com", "Quanchi", "Chen");
        CreateEventThread ep2 = new CreateEventThread
                ("ep2@gmail.com", "ep2@gmail.com", "Yijie", "Xie");
        ModifyEventThread ep3 = new ModifyEventThread
                ("ep3@gmail.com", "ep3@gmail.com", "Jingning", "Qian");
        ModifyEventThread ep4 = new ModifyEventThread
                ("ep4@gmail.com", "ep4@gmail.com", "Wenxuan", "Xie");

        // Create two customers.
        CustomerThread customer1 = new CustomerThread
                ("customer1@gmail.com", "customer1@gmail.com", "Toby", "Murray");
        CustomerThread customer2 = new CustomerThread
                ("customer2@gmail.com", "customer2@gmail.com", "Chris", "Ewin");

        // Let two event planners create a event and invite each other
        ep3.createEvent(56);
        ep4.createEvent(55);
        ep3.inviteEventPlanner(ep4.getMockEventPlanner());
        ep4.inviteEventPlanner(ep3.getMockEventPlanner());

        // Start the first two event planner threads that create events simultaneously.
        ep1.start();
        ep2.start();

        try {
            ep1.join();
            ep2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Start the last two event planner threads and the customer threads.
        ep3.start();
        ep4.start();
        customer1.start();
        customer2.start();

        try {
            ep3.join();
            ep4.join();
            customer1.join();
            customer2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        CancelOrderThread cancel1 = new CancelOrderThread(customer1.getCustomer());
        CancelOrderThread cancel2 = new CancelOrderThread(customer1.getCustomer());

        cancel1.start();
        cancel2.start();

        try{
            cancel1.join();
            cancel2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Concurrency Testing Ended");
    }
}
