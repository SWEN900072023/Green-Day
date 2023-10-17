package edu.unimelb.swen90007.mes.service.test;

public class ConcurrencyTest {
    public static void main(String[] args) {
        System.out.println("Concurrency Testing Started");

        // Simulate the administrator to create two venues.
        Admin.createVenue();

        // Create three event planners.
        CreateEventThread ep1 = new CreateEventThread
                ("ep1@gmail.com", "ep1@gmail.com", "Quanchi", "Chen");
        CreateEventThread ep2 = new CreateEventThread
                ("ep2@gmail.com", "ep2@gmail.com", "Yijie", "Xie");
        ModifyEventThread ep3 = new ModifyEventThread
                ("ep3@gmail.com", "ep3@gmail.com", "Jingning", "Qian");
        ModifyEventThread ep4 = new ModifyEventThread
                ("ep4@gmail.com", "123456", "Tony", "Sack");

        // Create two customers.
        CustomerThread customer1 = new CustomerThread
                ("customer1@gmail.com", "customer1@gmail.com", "Toby", "Murray");
        CustomerThread customer2 = new CustomerThread
                ("customer2@gmail.com", "customer2@gmail.com", "Chris", "Ewin");

        ep3.createEvent(5);
        ep4.createEvent(10);

        ep3.inviteEventPlanner(ep4.getMockEventPlanner());
        ep4.inviteEventPlanner(ep3.getMockEventPlanner());

        Admin.viewUsers();

        ep1.start();
        ep2.start();
        ep3.start();
        ep4.start();
        customer1.start();
        customer2.start();

        try {
            ep1.join();
            ep2.join();
            ep3.join();
            customer1.join();
            customer2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Concurrency Testing Ended");
    }
}
