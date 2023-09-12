package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class Event {
    private static final Logger logger = LogManager.getLogger(Event.class);
    private Integer id;
    private List<Section> sections;
    private String title;
    private String artist;
    private Venue venue;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String status;

    public Event(int id) {
        this.id = id;
    }

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

    public String getStatus() throws SQLException {
        if (status == null)
            load();
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        status = event.getStatus();
    }
}
