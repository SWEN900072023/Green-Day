package edu.unimelb.swen90007.mes.Lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockManager {
    private static LockManager instance;
    private final ConcurrentMap<Integer, ReadWriteLock> ticketsLocks;

    public static synchronized LockManager getInstance() {
        if(instance == null) {
            instance = new LockManager();
        }
        return instance;
    }

    private LockManager() {
        ticketsLocks = new ConcurrentHashMap<>();
    }

    public synchronized void acquireTicketsReadLock(int sectionId) {
        if (!ticketsLocks.containsKey(sectionId)) {
            ticketsLocks.put(sectionId, new ReentrantReadWriteLock());
        }
        Lock readLock = ticketsLocks.get(sectionId).readLock();
        readLock.lock();
    }

    public synchronized void releaseTicketsReadLock(int sectionId) {
        Lock readLock = ticketsLocks.get(sectionId).readLock();
        readLock.unlock();
    }

    public synchronized Lock getTicketsWriteLock(int sectionId) {
        if (!ticketsLocks.containsKey(sectionId)) {
            ticketsLocks.put(sectionId, new ReentrantReadWriteLock());
        }
        return ticketsLocks.get(sectionId).writeLock();
    }

}
