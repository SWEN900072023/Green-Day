package edu.unimelb.swen90007.mes.util;

import java.util.ArrayList;
import java.util.List;

/**
 * The unit of work class.<br>
 * We employ caller registration, i.e., callers are responsible for registering new/dirty objects with the unit of work. <br>
 * Note that it does not maintain a list of deleted objects since no business transaction needs to delete records from the database. <br>
 * After an event planner cancels the event, the system will update the event status to Cancelled in the table. <br>
 * After a customer cancels the order, the system will update the order status to Cancelled in the table.
 */
public class UnitOfWork {
    private static final ThreadLocal<UnitOfWork> current = new ThreadLocal<>();
    private static UnitOfWork instance;
    private final List<Object> newObjects = new ArrayList<>();
    private final List<Object> dirtyObjects = new ArrayList<>();

    private UnitOfWork() {

    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public static UnitOfWork getInstance() {
        if (instance == null)
            instance = new UnitOfWork();
        return instance;
    }

    public void registerNew(Object o) {
        if (newObjects.contains(o) || dirtyObjects.contains(o))
            return;
        newObjects.add(o);
    }

    public void registerDirty(Object o) {
        if (newObjects.contains(o) || dirtyObjects.contains(o))
            return;
        dirtyObjects.add(o);
    }

    public void commit() {

    }
}
