package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;

import java.util.List;

public class CreateEventThread extends EventPlannerThread {
    private static final int NUM_EVENTS = 50;
    private List<EventPlanner> invitedEventPlanners;

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
            for (EventPlanner anotherEventPlanner : invitedEventPlanners)
                inviteEventPlanner(anotherEventPlanner, event);
        }
    }

    public void setInvitedEventPlanners(List<EventPlanner> invitedEventPlanners) {
        this.invitedEventPlanners = invitedEventPlanners;
    }
}
