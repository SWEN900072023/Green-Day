package edu.unimelb.swen90007.mes.util;

import edu.unimelb.swen90007.mes.datamapper.*;
import edu.unimelb.swen90007.mes.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
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
    private final ArrayList<Object> deletedObjects = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(AppUserMapper.class);

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
        if (newObjects.remove(o)) {
            dirtyObjects.remove(o);
            return;
        }
        dirtyObjects.remove(o);
        if (!deletedObjects.contains(o)) {
            deletedObjects.add(o);
        }
    }

    public void commit() {
        for (Object object : newObjects) {
            try {
                if (object instanceof Event) {
                    EventMapper.create((Event) object);
                } else if (object instanceof Order) {
                    OrderMapper.create((Order) object);
                } else if (object instanceof SubOrder) {
                    SubOrderMapper.create((SubOrder) object);
                } else if (object instanceof Section) {
                    SectionMapper.create((Section) object);
                } else if (object instanceof Venue) {
                    VenueMapper.create((Venue) object);
                }
            } catch (SQLException e) {
                logger.error("UoW commit error: " + e.getMessage());
            }
        }
        newObjects.clear();

        for (Object object : dirtyObjects) {
            try {
                if (object instanceof Event) {
                    EventMapper.update((Event) object);
                } else if (object instanceof Order) {
                    OrderMapper.cancel((Order) object);
                }
            } catch (SQLException e) {
                logger.error("UoW commit error: " + e.getMessage());
            }
        }
        dirtyObjects.clear();

        for (Object object : deletedObjects) {
            try {
                if (object instanceof Event) {
                    EventMapper.delete((Event) object);
                } else if (object instanceof Order) {
                    OrderMapper.delete((Order) object);
                } else if (object instanceof Section) {
                    SectionMapper.delete((Section) object);
                } else if (object instanceof Venue) {
                    VenueMapper.delete((Venue) object);
                }
            } catch (SQLException e) {
                logger.error("UoW commit error: " + e.getMessage());
            }
        }
        deletedObjects.clear();
    }
}
