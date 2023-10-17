package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.VenueMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

@Getter
@Setter
public class Venue {
    private static final Logger logger = LogManager.getLogger(Venue.class);
    private Integer id;
    private String name;
    private String address;
    private Integer capacity;

    public Venue(Integer id) {
        this.id = id;
    }

    public Venue(Integer id, String name, String address, Integer capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public Venue(String name, String address, Integer capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public String loadName() {
        if (name == null)
            load();
        return name;
    }

    public String loadAddress() {
        if (address == null)
            load();
        return address;
    }

    public Integer loadCapacity() {
        if (capacity == null)
            load();
        return capacity;
    }

    private void load() {
        logger.info("Loading Venue [id=" + id + "]");
        try {
            Venue venue = VenueMapper.loadById(id);
            assert venue != null;
            name = venue.loadName();
            address = venue.loadAddress();
            capacity = venue.loadCapacity();
        } catch (SQLException e) {
            logger.error(String.format("Error loading Venue [id=%d]: %s", id, e.getMessage()));
        }
    }
}
