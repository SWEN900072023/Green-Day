package edu.unimelb.swen90007.mes.exceptions;

public class AppUserAlreadyExistsException extends Exception {
    public AppUserAlreadyExistsException() {
        super("App User Already Exists");
    }
}
