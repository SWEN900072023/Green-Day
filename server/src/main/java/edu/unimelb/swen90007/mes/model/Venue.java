package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.VenueMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Venue {
    private static final Logger logger = LogManager.getLogger(Venue.class);
    private Integer id;
    private String name;
    private String address;
    private Integer capacity;

    public Venue(int id) {
        this.id = id;
    }

    public Venue(int id, String name, String address, int capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public Venue(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (name == null)
            load();
        return name;
    }

    public String getAddress() {
        if (address == null)
            load();
        return address;
    }

    public int getCapacity() {
        if (capacity == null)
            load();
        return capacity;
    }

    private void load() {
        logger.info("Loading Venue [id=" + id + "]");
        try {
            Venue venue = VenueMapper.loadById(id);
            assert venue != null;
            name = venue.getName();
            address = venue.getAddress();
            capacity = venue.getCapacity();
        } catch (SQLException e) {
            logger.error(String.format("Error loading Venue [id=%d]: %s", id, e.getMessage()));
        }
    }
}
