package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.model.Event;

/**
 * Thread representing an event planner who only creates events in the concurrency test.
 */
public class CreateEventThread extends EventPlannerThread {
    private static final int NUM_EVENTS = 50;

    public CreateEventThread(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }

    /**
     * Simulate an event planner to create NUM_EVENTS different events and invite others.
     */
    @Override
    public void run() {
        for (int i = 0; i < NUM_EVENTS; i++) {
            Event event = createEvent(i);
            // Event creation fails due to time conflict.
            if (event == null)
                return;
        }
    }
}
