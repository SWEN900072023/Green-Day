package edu.unimelb.swen90007.mes.exceptions;

public class CapacityExceedsException extends Exception{
    public CapacityExceedsException() {
        super("Capacity exceeds limit");
    }
}
