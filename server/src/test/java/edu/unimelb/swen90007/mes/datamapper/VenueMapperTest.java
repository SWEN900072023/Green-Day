package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Venue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class VenueMapperTest {
    @Test
    void testCreate() throws SQLException {
        int id = 0;
        String name = "Wonderful Venue";
        String address = "388 Queensberry St, North Melbourne, 3051";
        int capacity = 1000;

        Venue venue = new Venue(id, name, address, capacity);
        VenueMapper.create(venue);

        Venue actualVenue = VenueMapper.loadByID(venue.getID());
        Assertions.assertEquals(venue.getID(), actualVenue.getID());
        Assertions.assertEquals(name, actualVenue.getName());
        Assertions.assertEquals(address, actualVenue.getAddress());
        Assertions.assertEquals(capacity, actualVenue.getCapacity());

        VenueMapper.delete(venue.getID());
    }
}