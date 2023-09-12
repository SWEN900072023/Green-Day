package edu.unimelb.swen90007.mes.exceptions;

public class AppUserNotFoundException extends Exception {
    public AppUserNotFoundException() {
        super("App User Not Found");
    }
}
