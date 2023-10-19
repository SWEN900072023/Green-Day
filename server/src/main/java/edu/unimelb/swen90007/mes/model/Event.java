package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Event {
    private static final Logger logger = LogManager.getLogger(Event.class);
    private Integer id;
    private Integer firstPlannerId;
    private List<Section> sections;
    private String title;
    private String artist;
    private Venue venue;
    private Integer status; // 1 : Within 6 Months, 2 : Out Of 6 Months, 3 : Ended, 4 : Cancelled
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, List<Section> sections, String title, String artist,
                 Venue venue, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.sections = sections;
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Event(Integer id, String title, String artist,
                 Venue venue, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Event(String title, String artist,
                 Venue venue, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.artist = artist;
        this.venue = venue;
        this.startTime = startTime;
        this.endTime = endTime;
        this.updateStatus(startTime);
    }

    public Integer updateStatus(LocalDateTime startTime) {
        if (startTime.isBefore(LocalDateTime.now()))
            this.status = Constant.EVENT_PAST;
        else if (startTime.isBefore(LocalDateTime.now().plusMonths(6)))
            this.status = Constant.EVENT_IN_SIX;
        else
            this.status = Constant.EVENT_OUT_SIX;
        return this.status;
    }

    public List<Section> loadSections() {
        if (sections == null)
            load();
        return sections;
    }

    public String loadTitle() {
        if (title == null)
            load();
        return title;
    }

    public String loadArtist() {
        if (artist == null)
            load();
        return artist;
    }

    public Venue loadVenue() {
        if (venue == null)
            load();
        return venue;
    }

    public Integer loadStatus() {
        if (status == null)
            load();
        return status;
    }

    public LocalDateTime loadStartTime() {
        if (startTime == null)
            load();
        return startTime;
    }

    public LocalDateTime loadEndTime() {
        if (endTime == null)
            load();
        return endTime;
    }

    private void load() {
        logger.info("Loading Event [id=" + id + "]");
        try {
            Event event = EventMapper.loadByIdAll(id);
            assert event != null;
            sections = event.loadSections();
            title = event.loadTitle();
            artist = event.loadArtist();
            venue = event.loadVenue();
            status = event.loadStatus();
            startTime = event.loadStartTime();
            endTime = event.loadEndTime();
        } catch (SQLException e) {
            logger.error(String.format("Error loading Event [id=%d]: %s", id, e.getMessage()));
        }
    }
}
