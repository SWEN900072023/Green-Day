package edu.unimelb.swen90007.mes.exceptions;

public class PermissionDeniedException extends Exception{
    public PermissionDeniedException() {
        super("Permission Denied");
    }
}
