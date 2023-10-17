package edu.unimelb.swen90007.mes.service.test;

public class ModifyEventThread extends EventPlannerThread {
    public ModifyEventThread(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            modifyEvent();
        }
    }
}
