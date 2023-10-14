package edu.unimelb.swen90007.mes.Lock;

import java.util.List;
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

    public void acquireTicketsReadLock(int sectionId) {
        if (!ticketsLocks.containsKey(sectionId)) {
            ticketsLocks.put(sectionId, new ReentrantReadWriteLock());
        }
        Lock readLock = ticketsLocks.get(sectionId).readLock();
        readLock.lock();
    }

    public void releaseTicketsReadLock(int sectionId) {
        Lock readLock = ticketsLocks.get(sectionId).readLock();
        readLock.unlock();
    }

    public void acquireTicketsWriteLock(List<Integer> sectionIds) {
        for(int sectionId : sectionIds){
            if (!ticketsLocks.containsKey(sectionId)) {
                ticketsLocks.put(sectionId, new ReentrantReadWriteLock());
            }
            Lock writeLock = ticketsLocks.get(sectionId).writeLock();
            writeLock.lock();
        }
    }

    public void releaseTicketsWriteLock(List<Integer> sectionIds) {
        for(int sectionId : sectionIds){
            Lock writeLock = ticketsLocks.get(sectionId).writeLock();
            writeLock.unlock();
        }
    }

}
