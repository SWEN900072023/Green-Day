package edu.unimelb.swen90007.mes.model;

import java.time.OffsetDateTime;
import java.util.List;

public class Event {
    private final int id;
    private final List<EventPlanner> eventPlanners;
    private final List<Section> sections;
    private String title;
    private String artist;
    private Venue venue;
    private OffsetDateTime datetime;
    private String status;

    public Event(int id, List<EventPlanner> eventPlanners, List<Section> sections, String title, String artist, Venue venue, OffsetDateTime datetime, String status) {
        this.id = id;
        this.eventPlanners = eventPlanners;
        this.sections = sections;
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.datetime = datetime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public List<EventPlanner> getEventPlanners() {
        return eventPlanners;
    }

    public List<Section> getSections() {
        return sections;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public OffsetDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(OffsetDateTime datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
