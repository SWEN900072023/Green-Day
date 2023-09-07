package edu.unimelb.swen90007.mes.model;

public class Venue {
    private final int id;
    private final String name;
    private final String address;
    private final int capacity;

    public Venue(int id, String name, String address, int capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }
}
