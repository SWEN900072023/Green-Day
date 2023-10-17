package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.exceptions.*;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.impl.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Thread representing an event planner.
 */
public class EventPlannerThread extends Thread {
    private final EventPlanner eventPlanner;
    private final PublicService publicService = new PublicService();
    private final EventPlannerService eventPlannerService = new EventPlannerService();
    private final Money money = new Money(new BigDecimal(100), "AUD");

    /**
     * In the constructor, simulate the administrator to register an event planner.
     *
     * @param email     the event planner's email
     * @param password  the event planner's password
     * @param firstName the event planner's first name
     * @param lastName  the event planner's last name
     */
    public EventPlannerThread(String email, String password, String firstName, String lastName) {
        eventPlanner = new EventPlanner(email, password, firstName, lastName);
        try {
            AppUserService appUserService = new AppUserService();
            appUserService.register(eventPlanner);
        } catch (SQLException | UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Simulate an event planner to create an event.
     *
     * @param days the number of days from the start of the event to now
     * @return the created event object
     */
    public Event createEvent(int days) {
        try {
            // Simulate an event planner to view all existing venues.
            List<Venue> venues = publicService.viewAllVenues();

            // Simulate an event planner to create an event.
            String title = eventPlanner.getFirstName() + eventPlanner.getLastName() + days;
            String artist = "Mock Artist";
            Venue venue = venues.get(0);
            LocalDateTime now = LocalDateTime.now();
            Event event = new Event
                    (title, artist, venue, now.plusDays(days).plusHours(1), now.plusDays(days).plusHours(5));

            List<Section> sections = new ArrayList<>();
            sections.add(new Section(event, event.getTitle() + "Bronze", money, 30, 30));
            sections.add(new Section(event, event.getTitle() + "Silver", money, 20, 20));
            sections.add(new Section(event, event.getTitle() + "Gold", money, 10, 10));
            sections.add(new Section(event, event.getTitle() + "VIP", money, 5, 5));
            event.setSections(sections);
            event.setFirstPlannerId(eventPlanner.getId());
            eventPlannerService.createEvent(event);
            return event;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TimeConflictException | InvalidTimeRangeException e) {
            System.out.println("Time Conflict");
        } catch (CapacityExceedsException e) {
            System.out.println("Capacity Exceeds");
        }
        return null;
    }

    /**
     * Simulate an event planner to invite another event planner.
     *
     * @param another another event planner
     */
    public void inviteEventPlanner(EventPlanner another, Event event) {
        try {
            eventPlannerService.inviteEventPlanner(eventPlanner, another, event);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (PermissionDeniedException e) {
            System.out.println("Permission Denied");
        }
    }

    /**
     * Simulate an event planner to modify a random hosted event.
     */
    public void modifyEvent() {
        try {
            List<Event> events = eventPlannerService.viewHostedEvent(eventPlanner);
            int size = events.size();
            int index = ThreadLocalRandom.current().nextInt(size);
            Event event = events.get(index);
            event.setArtist(eventPlanner.getFirstName() + ThreadLocalRandom.current().nextInt(100));
            for (Section section : event.loadSections()) {
                int update = ThreadLocalRandom.current().nextBoolean() ? -1 : 1;
                section.setCapacity(section.getCapacity() + update);
            }
            eventPlannerService.modifyEvent(eventPlanner, event);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TimeConflictException e) {
            System.out.println("Time Conflict");
        } catch (CapacityExceedsException e) {
            System.out.println("Capacity Exceeds");
        } catch (PermissionDeniedException e) {
            System.out.println("Permission Denied");
        } catch (InvalidTimeRangeException e) {
            System.out.println("Invalid Time Range");
        }
    }

    public EventPlanner getMockEventPlanner() {
        return eventPlanner;
    }
}
