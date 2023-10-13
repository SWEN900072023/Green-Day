package edu.unimelb.swen90007.mes.service.test;

public class ServiceTest {
    public static void main(String[] args){
        Admin.createVenue();
        EventPlannerThread ep1 = new EventPlannerThread
                ("AAA@XXX.XXX", "123456", "Bruce", "Wayne");
        EventPlannerThread ep2 = new EventPlannerThread
                ("BBB@XXX.XXX", "123456", "Tony", "Sack");
        CustomerThread customer1 = new CustomerThread
                ("CCC@XXX.XXX", "123456", "Peter", "Park");
        CustomerThread customer2 = new CustomerThread
                ("DDD@XXX.XXX", "123456", "Ethan", "Winters");
        ep1.createEvents(5);
        ep1.createEvents(6);
        ep2.createEvents(10);
        ep2.createEvents(11);

        ep1.start();
        ep2.start();
        customer1.start();
        customer2.start();
    }
}
