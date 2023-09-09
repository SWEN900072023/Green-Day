package edu.unimelb.swen90007.mes.model;

import java.time.OffsetDateTime;
import java.util.List;

public class Event {
    private final List<Section> sections;
    private int id;
    private String title;
    private String artist;
    private Venue venue;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;

    public Event(int id, List<Section> sections, String title, String artist, Venue venue, OffsetDateTime startTime, OffsetDateTime endTime, String status) {
        this.id = id;
        this.sections = sections;
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }
}
