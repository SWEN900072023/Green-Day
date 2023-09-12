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
import java.util.Date;
import java.util.List;

class EventMapperTest {
    static EventPlanner eventPlanner;
    static Venue venue;

    @BeforeAll
    static void createEventPlannerAndVenue() throws SQLException, UserAlreadyExistsException {
        eventPlanner = new EventPlanner(0, "event.planner@gmail.com", "mock.hash", "Quanchi", "Chen");
        AppUserMapper.create(eventPlanner);

        venue = new Venue(0, "Mock Venue", "Mock Address", 1000);
        VenueMapper.create(venue);
    }

    @AfterAll
    static void deleteEventPlannerAndVenue() throws SQLException {
        AppUserMapper.delete(eventPlanner.getId());
        VenueMapper.delete(venue.getId());
    }

    @Test
    void testCreate() throws SQLException {
        String section1Name = "Section 1";
        BigDecimal section1UnitPrice = BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP);
        String section1Currency = "AUD";
        Money section1Money = new Money(section1UnitPrice, section1Currency);
        int section1Capacity = 600;
        int section1RemainingTickets = 600;
        Section section1 = new Section(0, null, section1Name, section1Money, section1Capacity, section1RemainingTickets);

        String section2Name = "Section 2";
        BigDecimal section2UnitPrice = BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP);
        String section2Currency = "AUD";
        Money section2Money = new Money(section2UnitPrice, section2Currency);
        int section2Capacity = 400;
        int section2RemainingTickets = 400;
        Section section2 = new Section(0, null, section2Name, section2Money, section2Capacity, section2RemainingTickets);

        List<Section> sections = new ArrayList<>();
        sections.add(section1);
        sections.add(section2);

        String title = "Mock Event";
        String artist = "Mock Artist";
        OffsetDateTime startTime = new Date().toInstant().atOffset(ZoneOffset.UTC);
        OffsetDateTime endTime = startTime.plusHours(2);
        String status = "Active";

        Event event = new Event(0, sections, title, artist, venue, startTime, endTime, status);
        EventMapper.create(eventPlanner.getId(), event);

        Event actualEvent = EventMapper.loadById(event.getId());
        List<Section> actualSections = actualEvent.getSections();
        Section actualSection1 = actualSections.get(0);
        Section actualSection2 = actualSections.get(1);

        Assertions.assertEquals(section1Name, actualSection1.getName());
        Assertions.assertEquals(section1UnitPrice, actualSection1.getMoney().getUnitPrice());
        Assertions.assertEquals(section1Currency, actualSection1.getMoney().getCurrency());
        Assertions.assertEquals(section1Capacity, actualSection1.getCapacity());
        Assertions.assertEquals(section1RemainingTickets, actualSection1.getRemainingTickets());

        Assertions.assertEquals(section2Name, actualSection2.getName());
        Assertions.assertEquals(section2UnitPrice, actualSection2.getMoney().getUnitPrice());
        Assertions.assertEquals(section2Currency, actualSection2.getMoney().getCurrency());
        Assertions.assertEquals(section2Capacity, actualSection2.getCapacity());
        Assertions.assertEquals(section2RemainingTickets, actualSection2.getRemainingTickets());

        Assertions.assertEquals(title, actualEvent.getTitle());
        Assertions.assertEquals(artist, actualEvent.getArtist());
        Assertions.assertEquals(status, actualEvent.getStatus());

        for (Section section : actualSections)
            SectionMapper.delete(section.getId());
        EventMapper.delete(actualEvent.getId(), eventPlanner.getId());
    }
}