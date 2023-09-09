package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

class EventMapperTest {
    static EventPlanner eventPlanner;
    static Venue venue;

    @BeforeAll
    static void createEventPlannerAndVenue() throws SQLException, UserAlreadyExistsException {
        eventPlanner = new EventPlanner(0, "mock@gmail.com", "mock.hash", "Quanchi", "Chen");
        UserMapper.create(eventPlanner);

        venue = new Venue(0, "Mock Venue", "Mock Address", 1000);
        VenueMapper.create(venue);
    }

    @AfterAll
    static void deleteEventPlannerAndVenue() throws SQLException {
        UserMapper.deleteByID(eventPlanner.getID());
        VenueMapper.delete(venue.getID());
    }

    @Test
    void create() throws SQLException {
        int id = 0;
        String title = "Mock Event";
        String artist = "Mock Artist";
        OffsetDateTime startTime = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime endTime = OffsetDateTime.now(ZoneOffset.UTC);
        String status = "Active";
        String sectionName = "Mock Section";
        BigDecimal unitPrice = BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP);
        String currency = "AUD";

        Money money = new Money(unitPrice, currency);
        Section section = new Section(id, null, sectionName, money, venue.getCapacity(), venue.getCapacity());
        List<Section> sections = new ArrayList<>();
        sections.add(section);
        Event event = new Event(id, sections, title, artist, venue, startTime, endTime, status);
        for (Section singleSection : sections)
            singleSection.setEvent(event);

        EventMapper.create(eventPlanner.getID(), event);

        Event actualEvent = EventMapper.loadByID(event.getID());
        Assertions.assertEquals(event.getID(), actualEvent.getID());
        Assertions.assertEquals(title, actualEvent.getTitle());
        Assertions.assertEquals(artist, actualEvent.getArtist());
        Assertions.assertEquals(status, actualEvent.getStatus());

        Section actualSection = actualEvent.getSections().get(0);
        Assertions.assertEquals(section.getID(), actualSection.getID());
        Assertions.assertEquals(sectionName, actualSection.getName());
        Assertions.assertEquals(venue.getCapacity(), actualSection.getCapacity());
        Assertions.assertEquals(venue.getCapacity(), actualSection.getCapacity());

        Money actualMoney = actualSection.getMoney();
        Assertions.assertEquals(unitPrice, actualMoney.getUnitPrice());
        Assertions.assertEquals(currency, actualMoney.getCurrency());

        Venue actualVenue = actualEvent.getVenue();
        Assertions.assertEquals(venue.getID(), actualVenue.getID());
        Assertions.assertEquals(venue.getName(), actualVenue.getName());
        Assertions.assertEquals(venue.getAddress(), actualVenue.getAddress());
        Assertions.assertEquals(venue.getCapacity(), actualVenue.getCapacity());

        SectionMapper.delete(section.getID());
        EventMapper.delete(event.getID(), eventPlanner.getID());
    }

    @Test
    void loadByEventPlanner() {
    }

    @Test
    void loadByPattern() {
    }

    @Test
    void update() {
    }
}