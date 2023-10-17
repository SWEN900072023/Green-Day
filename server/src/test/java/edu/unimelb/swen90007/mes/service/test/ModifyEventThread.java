package edu.unimelb.swen90007.mes.service.test;

public class ModifyEventThread extends EventPlannerThread {
    private static final int NUM_MODIFICATIONS = 20;

    public ModifyEventThread(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }

    /**
     * Simulate an event planner to perform NUM_MODIFICATIONS modifications to potentially different hosted events.
     */
    @Override
    public void run() {
        for (int i = 0; i < NUM_MODIFICATIONS; i++) {
            modifyEvent();
        }
    }
}
