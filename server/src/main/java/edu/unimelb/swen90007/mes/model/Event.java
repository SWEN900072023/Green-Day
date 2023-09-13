package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class Event {
    private static final Logger logger = LogManager.getLogger(Event.class);
    private int id;
    private int firstPlannerId;
    private List<Section> sections;
    private String title;
    private String artist;
    private Venue venue;
    private Integer status; // 1 : Coming Soon, 2 : Long After, 3 : Ended
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    public Event(int id) {
        this.id = id;
    }

    public Event(int id, List<Section> sections, String title, String artist,
                 Venue venue, int status, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.id = id;
        this.sections = sections;
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Event(String title, String artist,
                 Venue venue, OffsetDateTime startTime, OffsetDateTime endTime) {
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.startTime = startTime;
        this.endTime = endTime;
        OffsetDateTime now = OffsetDateTime.now();
        if(startTime.isBefore(now))
            this.status = 3;
        else if (startTime.isBefore(now.plusMonths(6)))
            this.status = 1;
        else
            this.status = 2;
    }

    public int getFirstPlannerId() { return firstPlannerId; }
    public void setFirstPlannerId(int id) { this.firstPlannerId = id; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Section> getSections() throws SQLException {
        if (sections == null)
            load();
        return sections;
    }

    public void setSections(List<Section> sections) throws SQLException {
        this.sections = sections;
    }

    public String getTitle() throws SQLException {
        if (title == null)
            load();
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() throws SQLException {
        if (artist == null)
            load();
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Venue getVenue() throws SQLException {
        if (venue == null)
            load();
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getStatus() throws SQLException {
        if (status == null)
            load();
        return status;
    }

    public OffsetDateTime getStartTime() throws SQLException {
        if (startTime == null)
            load();
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getEndTime() throws SQLException {
        if (endTime == null)
            load();
        return endTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    private void load() throws SQLException {
        logger.info("Loading Event [id=" + id + "]");
        Event event = EventMapper.loadById(id);
        assert event != null;
        sections = event.getSections();
        title = event.getTitle();
        artist = event.getArtist();
        venue = event.getVenue();
        startTime = event.getStartTime();
        endTime = event.getEndTime();
    }
}
