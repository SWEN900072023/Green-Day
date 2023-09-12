package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.Section;
import edu.unimelb.swen90007.mes.model.Venue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class EventMapper {
    private static final Logger logger = LogManager.getLogger(EventMapper.class);

    public static void create(int eventPlannerId, Event event) throws SQLException {
        String sql = "INSERT INTO events (title, artist, venue_id, start_time, end_time, status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getArtist());
        preparedStatement.setInt(3, event.getVenue().getId());
        preparedStatement.setObject(4, event.getStartTime());
        preparedStatement.setObject(5, event.getEndTime());
        preparedStatement.setString(6, "Active");
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            event.setId(generatedKeys.getInt("id"));
        logger.info("New Event Created [id=" + event.getId() + "]");

        sql = "INSERT INTO planner_events (event_id, planner_id) VALUES (?, ?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, event.getId());
        preparedStatement.setInt(2, eventPlannerId);
        preparedStatement.executeUpdate();
        logger.info("New Association Created [event_id=" + event.getId() + "], [planner_id=" + eventPlannerId + "]");

        List<Section> sections = event.getSections();
        for (Section section : sections) {
            SectionMapper.create(section);
            section.setEvent(event);
        }
    }

    public static List<Event> loadAll() throws SQLException {
        String sql = "SELECT * FROM events";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static List<Event> loadNextSixMonths() throws SQLException {
        String sql = "SELECT * FROM events WHERE end_time <= ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, OffsetDateTime.now().plusMonths(6));
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static List<Event> loadByPattern(String pattern) throws SQLException {
        pattern = "%" + pattern + "%";
        String sql = "SELECT * FROM events WHERE title LIKE ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, pattern);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static Event loadById(int eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
    }

    private static List<Event> load(ResultSet resultSet) throws SQLException {
        List<Event> events = new ArrayList<>();

        while (resultSet.next()) {
            int eventId = resultSet.getInt("id");
            String title = resultSet.getString("title").trim();
            String artist = resultSet.getString("artist").trim();
            int venueId = resultSet.getInt("venue_id");
            OffsetDateTime startTime = resultSet.getObject("start_time", OffsetDateTime.class);
            OffsetDateTime endTime = resultSet.getObject("end_time", OffsetDateTime.class);
            String status = resultSet.getString("status").trim();

            List<Section> sections = new ArrayList<>();
            List<Integer> sectionIds = SectionMapper.loadSectionIdsByEventId(eventId);
            for (Integer sectionId : sectionIds)
                sections.add(new Section(sectionId));

            Venue venue = new Venue(venueId);

            Event event = new Event(eventId, sections, title, artist, venue, startTime, endTime, status);

            for (Section section : sections)
                section.setEvent(event);

            logger.info("Event Loaded [id=" + eventId + "]");
        }

        return events;
    }

    public static void update(Event event) throws SQLException {
        String sql = "UPDATE events SET title = ?, artist = ?, venue_id = ?, start_time = ?, end_time = ?, status = ? WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getArtist());
        preparedStatement.setInt(3, event.getVenue().getId());
        preparedStatement.setObject(4, event.getStartTime());
        preparedStatement.setObject(5, event.getEndTime());
        preparedStatement.setString(6, event.getStatus());
        preparedStatement.setInt(7, event.getId());
        preparedStatement.executeUpdate();

        for (Section section : event.getSections())
            SectionMapper.update(section);

        logger.info("Event Updated [id=" + event.getId() + "]");
    }

    public static void delete(int eventId, int plannerId) throws SQLException {
        String sql = "DELETE FROM planner_events WHERE event_id = ? AND planner_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        preparedStatement.setInt(2, plannerId);
        preparedStatement.executeUpdate();
        logger.info("Association Deleted [event_id=" + eventId + "], [planner_id=" + plannerId + "]");

        sql = "DELETE FROM events WHERE id = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        preparedStatement.executeUpdate();
        logger.info("Existing Event Deleted [id=" + eventId + "]");
    }
}
