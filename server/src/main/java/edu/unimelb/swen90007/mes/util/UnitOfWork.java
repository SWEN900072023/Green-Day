package edu.unimelb.swen90007.mes.util;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private static final ThreadLocal<UnitOfWork> current = new ThreadLocal<>();
    private final List<Object> newObjects = new ArrayList<>();
    private final List<Object> dirtyObjects = new ArrayList<>();
    private final List<Object> deletedObjects = new ArrayList<>();

    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static UnitOfWork getCurrent() {
        return (UnitOfWork) current.get();
    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public void registerNew(Object obj) {

    }

    public void registerDirty(Object obj) {

    }

    public void registerDeleted(Object obj) {

    }

    public void commit() {

    }
}
