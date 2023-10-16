package edu.unimelb.swen90007.mes.service.test;

public class ServiceTest {
    public static void main(String[] args){
        Admin.createVenue();
        CreateEventThread ep1 = new CreateEventThread
                ("ep1@XXX.XXX", "123456", "Bruce", "Wayne");
        CreateEventThread ep2 = new CreateEventThread
                ("ep2@XXX.XXX", "123456", "Tony", "Sack");
        ModifyEventThread ep3 = new ModifyEventThread
                ("ep3@XXX.XXX", "123456", "Tony", "Sack");
        ModifyEventThread ep4 = new ModifyEventThread
                ("ep4@XXX.XXX", "123456", "Tony", "Sack");
        CustomerThread customer1 = new CustomerThread
                ("CCC1@XXX.XXX", "123456", "Peter", "Park");
        CustomerThread customer2 = new CustomerThread
                ("CCC2@XXX.XXX", "123456", "Ethan", "Winters");
        ep3.createEvents(5);
        ep4.createEvents(10);
        ep3.inviteEventPlanner(ep4.getEP());
        ep4.inviteEventPlanner(ep3.getEP());
        Admin.viewUsers();

        ep1.start();
        ep2.start();
        ep3.start();
        ep4.start();
        customer1.start();
        customer2.start();
    }
}
