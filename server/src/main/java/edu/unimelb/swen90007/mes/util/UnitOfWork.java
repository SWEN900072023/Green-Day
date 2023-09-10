package edu.unimelb.swen90007.mes.util;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private static final ThreadLocal<UnitOfWork> current = new ThreadLocal<>();
    private static UnitOfWork instance;
    private final List<Object> newObjects = new ArrayList<>();
    private final List<Object> dirtyObjects = new ArrayList<>();
    private final List<Object> deletedObjects = new ArrayList<>();

    private UnitOfWork() {

    }

    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static UnitOfWork getCurrent() {
        return (UnitOfWork) current.get();
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
        if (newObjects.contains(o) || dirtyObjects.contains(o) || deletedObjects.contains(o))
            return;
        newObjects.add(o);
    }

    public void registerDirty(Object o) {
        if (newObjects.contains(o) || dirtyObjects.contains(o) || deletedObjects.contains(o))
            return;
        dirtyObjects.add(o);
    }

    public void registerDeleted(Object o) {
        if (newObjects.remove(o))
            return;
        dirtyObjects.remove(o);
        if (!deletedObjects.contains(o))
            deletedObjects.add(o);
    }

    public void commit() {

    }
}
