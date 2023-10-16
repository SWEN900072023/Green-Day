package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.datamapper.DBConnection;
import edu.unimelb.swen90007.mes.exceptions.*;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.impl.*;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventPlannerThread extends Thread{
    private final EventPlanner ep;
    private final PublicService publicService = new PublicService();
    private final EventPlannerService eventPlannerService = new EventPlannerService();
    private final Money money = new Money(new BigDecimal(100), "Australia");

    public EventPlannerThread(String email, String password, String firstName, String lastName) {
        ep = new EventPlanner(email, password, firstName, lastName);
        try{
            AppUserService appUserService = new AppUserService();
            appUserService.register(ep);
        } catch (SQLException | UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    public void createEvents(int days) {
        try{
            List<Venue> venues = publicService.viewAllVenues();
            int size = venues.size();
            // Create Events
            Random r = new Random();
            int index = r.nextInt(size);
            Venue venue = venues.get(index);
            LocalDateTime now = LocalDateTime.now();
            Event event = new Event
                    ("Title" + ep.getLastName(), ep.getFirstName(), venue, now.plusDays(days), now.plusDays(days).plusHours(5));

            // Create Sections
            List<Section> sections = new ArrayList<>();
            sections.add(new Section(event, "Normal", money, 200, 200));
            sections.add(new Section(event, "Special", money, 100, 100));
            sections.add(new Section(event, "VIP", money, 50, 50));
            sections.add(new Section(event, "V-VIP", money, 20, 20));
            event.setSections(sections);
            event.setFirstPlannerId(ep.getId());
            eventPlannerService.createEvent(event);

        } catch (TimeConflictException | SQLException |
                 InvalidTimeRangeException e) {
            throw new RuntimeException(e);
        } catch (CapacityExceedsException e) {
            System.out.println(ep.getFirstName() + " cannot update capacity. The capacity exceeds the limitation");
        }
    }

    public EventPlanner getEP() {
        return ep;
    }

    public void inviteEventPlanner(EventPlanner another) {
        try{
            List<Event> events = eventPlannerService.viewHostedEvent(ep);
            for(Event event : events){
                try{
                    eventPlannerService.inviteEventPlanner(ep, another, event);
                } catch (PermissionDeniedException e) {
                    System.out.println("Permission Denied");
                } catch (SQLException e){
                    System.out.println("relation already exist");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifyEvent() {
        try{
            List<Event> events = eventPlannerService.viewHostedEvent(ep);
            int size = events.size();
            Random r = new Random();
            int index = r.nextInt(size);
            Event e = events.get(index);
            e.setArtist(ep.getFirstName() + r.nextInt(100));
            for(Section section : e.loadSections()){
                int update = r.nextBoolean() ? -1 : 1;
                section.setCapacity(section.getCapacity() + update);
            }
            eventPlannerService.modifyEvent(ep, e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TimeConflictException e) {
            System.out.println("Time Conflict");
            throw new RuntimeException(e);
        } catch (CapacityExceedsException e) {
            System.out.println("Capacity Exceeds");
            throw new RuntimeException(e);
        } catch (PermissionDeniedException e) {
            System.out.println("Permission Denied");
            throw new RuntimeException(e);
        } catch (InvalidTimeRangeException e) {
            System.out.println("Invalid Time Range");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){
        for(int i = 0; i < 100; i++){
            modifyEvent();
        }
    }
}
