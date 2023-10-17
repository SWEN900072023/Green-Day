package edu.unimelb.swen90007.mes.service.test;

public class CreateEventThread extends EventPlannerThread {
    public CreateEventThread(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            createEvent(i);
        }
    }
}
