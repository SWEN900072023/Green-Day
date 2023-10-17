package edu.unimelb.swen90007.mes.service.test;

public class CreateEventThread extends EventPlannerThread {
    private static final int NUM_EVENTS = 50;

    public CreateEventThread(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }

    /**
     * Simulate an event planner to create NUM_EVENTS different events.
     */
    @Override
    public void run() {
        for (int i = 0; i < NUM_EVENTS; i++) {
            createEvent(i);
        }
    }
}
