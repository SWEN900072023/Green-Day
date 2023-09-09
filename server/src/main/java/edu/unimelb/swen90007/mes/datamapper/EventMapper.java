package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.Section;
import edu.unimelb.swen90007.mes.model.Venue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.List;

public final class EventMapper {
    private static final Logger logger = LogManager.getLogger(EventMapper.class);

    public static void create(int eventPlannerID, Event event) throws SQLException {
        String sql = "INSERT INTO events (title, artist, venue_id, start_time, end_time, status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getArtist());
        preparedStatement.setInt(3, event.getVenue().getID());
        preparedStatement.setObject(4, event.getStartTime());
        preparedStatement.setObject(5, event.getEndTime());
        preparedStatement.setString(6, "Active");
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            event.setID(generatedKeys.getInt("id"));
        logger.info("New Event Created [id=" + event.getID() + "]");

        sql = "INSERT INTO planner_events (event_id, planner_id) VALUES (?, ?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, event.getID());
        preparedStatement.setInt(2, eventPlannerID);
        preparedStatement.executeUpdate();
        logger.info("New Association Created [event_id=" + event.getID() + "], [planner_id=" + eventPlannerID + "]");

        List<Section> sections = event.getSections();
        for (Section section : sections) {
            SectionMapper.create(section);
        }
    }

    public static List<Event> loadAll() {
        return null;
    }

    public static List<Event> loadByEventPlanner(int eventPlannerID) {
        return null;
    }

    public static List<Event> loadByPattern(String pattern) {
        return null;
    }

    public static Event loadByID(int eventID) throws SQLException {
        Event event = null;

        String sql = "SELECT * FROM events WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String title = resultSet.getString("title").trim();
            String artist = resultSet.getString("artist").trim();
            int venueID = resultSet.getInt("venue_id");
            OffsetDateTime startTime = resultSet.getObject("start_time", OffsetDateTime.class);
            OffsetDateTime endTime = resultSet.getObject("end_time", OffsetDateTime.class);
            String status = resultSet.getString("status").trim();

            List<Section> sections = SectionMapper.loadByEventID(eventID);
            Venue venue = VenueMapper.loadByID(venueID);

            event = new Event(eventID, sections, title, artist, venue, startTime, endTime, status);

            for (Section section : sections)
                section.setEvent(event);

            logger.info("Event Loaded [id=" + eventID + "]");
        }

        return event;
    }

    public static void update(Event event) {

    }

    public static void delete(int eventID, int plannerID) throws SQLException {
        String sql = "DELETE FROM planner_events WHERE event_id = ? AND planner_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventID);
        preparedStatement.setInt(2, plannerID);
        preparedStatement.executeUpdate();
        logger.info("Association Deleted [event_id=" + eventID + "], [planner_id=" + plannerID + "]");

        sql = "DELETE FROM events WHERE id = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventID);
        preparedStatement.executeUpdate();
        logger.info("Existing Event Deleted [id=" + eventID + "]");
    }
}
