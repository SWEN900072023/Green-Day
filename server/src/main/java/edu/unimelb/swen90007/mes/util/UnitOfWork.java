package edu.unimelb.swen90007.mes.util;

import edu.unimelb.swen90007.mes.datamapper.*;
import edu.unimelb.swen90007.mes.exceptions.TimeConflictException;
import edu.unimelb.swen90007.mes.exceptions.VersionUnmatchedException;
import edu.unimelb.swen90007.mes.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The unit of work class.<br>
 * We employ caller registration, i.e., callers are responsible for registering new/dirty objects with the unit of work. <br>
 */
public class UnitOfWork {
    private static final ThreadLocal<UnitOfWork> current = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(UnitOfWork.class);
    private final List<Object> newObjects = new ArrayList<>();
    private final List<Object> dirtyObjects = new ArrayList<>();
    private final ArrayList<Object> deletedObjects = new ArrayList<>();

    private UnitOfWork() {
    }

    public static UnitOfWork getInstance() {
        if (current.get() == null)
            current.set(new UnitOfWork());
        return current.get();
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

    public void clear() {
        newObjects.clear();
        dirtyObjects.clear();
        deletedObjects.clear();
    }

    public void commit() {
        Connection connection = getConnection();
        if (connection == null) return;

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

            connection.commit();

        } catch (SQLException | VersionUnmatchedException | TimeConflictException e) {
            logger.error("UoW commit error: " + e.getMessage());
            try {
                logger.info("Rolling back the transaction......");
                connection.rollback();
            } catch (SQLException e1) {
                logger.error("UoW commit failed to rollback: " + e.getMessage());
            }
        } finally {
            clear();
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Failed to set connection automatic commit: " + e.getMessage());
            }
        }
    }

    private Connection getConnection() {
        try {
            return DBConnection.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to get a database connection: " + e.getMessage());
            return null;
        }
    }
}
