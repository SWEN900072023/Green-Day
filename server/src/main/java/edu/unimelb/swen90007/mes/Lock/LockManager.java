package edu.unimelb.swen90007.mes.Lock;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.Order;
import edu.unimelb.swen90007.mes.model.Section;
import edu.unimelb.swen90007.mes.model.SubOrder;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockManager {
    private static LockManager instance;
    public final Lock validationLock = new ReentrantLock();
    private final ConcurrentMap<Integer, ReadWriteLock> ticketsLocks;

    private LockManager() {
        ticketsLocks = new ConcurrentHashMap<>();
    }

    public static synchronized LockManager getInstance() {
        if (instance == null) {
            instance = new LockManager();
        }
        return instance;
    }

    public void acquireTicketsReadLock(Event event) {
        try {
            if (event.getSections() == null)
                event = EventMapper.loadByIdAll(event.getId());
            for (Section section : event.getSections()) {
                if (!ticketsLocks.containsKey(section.getId())) {
                    ticketsLocks.put(section.getId(), new ReentrantReadWriteLock());
                }
                Lock readLock = ticketsLocks.get(section.getId()).readLock();
                readLock.lock();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseTicketsReadLock(Event event) {
        for (Section section : event.getSections()) {
            Lock readLock = ticketsLocks.get(section.getId()).readLock();
            readLock.unlock();
        }
    }

    public void acquireTicketsWriteLock(Order order) {
        for (SubOrder subOrder : order.getSubOrders()) {
            int sectionId = subOrder.getSection().getId();
            if (!ticketsLocks.containsKey(sectionId)) {
                ticketsLocks.put(sectionId, new ReentrantReadWriteLock());
            }
            Lock writeLock = ticketsLocks.get(sectionId).writeLock();
            writeLock.lock();
        }
    }

    public void releaseTicketsWriteLock(Order order) {
        for (SubOrder subOrder : order.getSubOrders()) {
            int sectionId = subOrder.getSection().getId();
            Lock writeLock = ticketsLocks.get(sectionId).writeLock();
            writeLock.unlock();
        }
    }

}
