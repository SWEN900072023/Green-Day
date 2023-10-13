package edu.unimelb.swen90007.mes.util;

import edu.unimelb.swen90007.mes.datamapper.*;
import edu.unimelb.swen90007.mes.exceptions.VersionUnmatchedException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.Lock.LockManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;

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

    private static final Logger logger = LogManager.getLogger(UnitOfWork.class);

    private UnitOfWork() {
    }

    public static void setCurrent() {
        current.set(new UnitOfWork());
    }

    public static UnitOfWork getInstance() {
        if (current.get() == null) {
            if (instance == null)
                instance = new UnitOfWork();
            return instance;
        }
        else return current.get();
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
        Connection connection = getConnection();
        if (connection == null) return;

        List<Lock> lockList = acquireWriteLocks();

        try {
            connection.setAutoCommit(false);

            for (Object object : newObjects) {
                if (object instanceof Event) {
                    EventMapper.create((Event) object, connection);
                } else if (object instanceof Order) {
                    OrderMapper.create((Order) object, connection);
                } else if (object instanceof SubOrder) {
                    SubOrderMapper.create((SubOrder) object, connection);
                } else if (object instanceof Section) {
                    SectionMapper.create((Section) object, connection);
                } else if (object instanceof Venue) {
                    VenueMapper.create((Venue) object, connection);
                }
            }
            newObjects.clear();

            for (Object object : dirtyObjects) {
                if (object instanceof Event) {
                    EventMapper.update((Event) object, connection);
                } else if (object instanceof Section) {
                    SectionMapper.update((Section) object, connection);
                } else if (object instanceof SectionTickets) {
                    SectionMapper.ticketsUpdate((SectionTickets) object, connection);
                } else if (object instanceof Order) {
                    OrderMapper.cancel((Order) object, connection);
                }
            }
            dirtyObjects.clear();

            for (Object object : deletedObjects) {
                if (object instanceof Event) {
                    EventMapper.delete((Event) object, connection);
                } else if (object instanceof Order) {
                    OrderMapper.delete((Order) object, connection);
                } else if (object instanceof Section) {
                    SectionMapper.delete((Section) object, connection);
                } else if (object instanceof Venue) {
                    VenueMapper.delete((Venue) object, connection);
                }
            }
            deletedObjects.clear();

            connection.commit();

        } catch (SQLException | VersionUnmatchedException e) {
            try {
                System.out.println("Rolling back transaction: " + e.getMessage());
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("UoW commit failed to rollback: " + e.getMessage());
            }
        } finally {
            releaseWriteLocks(lockList);
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Failed to set connection automatic commit: " + e.getMessage());
            }
        }
    }

    public Connection getConnection(){
        try{
            return DBConnection.getConnection();
        } catch (SQLException e){
            logger.error("Failed to get a database connection: " + e.getMessage());
            return null;
        }
    }

    public List<Lock> acquireWriteLocks() {
        List<Lock> lockList = new LinkedList<>();
        for (Object object : dirtyObjects) {
            if (object instanceof SectionTickets) {
                int sectionId = ((SectionTickets) object).sectionId;
                Lock writeLock = LockManager.getInstance().getTicketsWriteLock(sectionId);
                writeLock.lock();
                lockList.add(writeLock);
            }
        }
        return lockList;
    }

    public void releaseWriteLocks(List<Lock> lockList) {
        for(Lock lock : lockList) {
            lock.unlock();
        }
    }
}
