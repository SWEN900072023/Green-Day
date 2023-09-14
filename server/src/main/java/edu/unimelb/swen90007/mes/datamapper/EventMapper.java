package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;
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

    public static void create(Event event) throws SQLException {
        String sql = "INSERT INTO events (title, artist, venue_id, status, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getArtist());
        preparedStatement.setInt(3, event.getVenue().getId());
        preparedStatement.setInt(4, event.getStatus());
        preparedStatement.setObject(5, event.getStartTime());
        preparedStatement.setObject(6, event.getEndTime());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            event.setId(generatedKeys.getInt("id"));
        logger.info("New Event Created [id=" + event.getId() + "]");

        PlannerEventMapper.create(event.getId(), event.getFirstPlannerId());

        List<Section> sections = event.getSections();
        for (Section section : sections) {
            section.setEvent(event);
            SectionMapper.create(section);
            section.setEvent(event);
        }
    }

    public static List<Event> loadAll() throws SQLException {
        String sql = "SELECT * FROM events WHERE status <> 3 ORDER BY start_time";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static List<Event> loadNextSixMonths() throws SQLException {
        String sql = "SELECT * FROM events WHERE status = 1 ORDER BY start_time";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static void updateExpiredEvent() throws SQLException {
        String sql = "UPDATE events SET status = 3 WHERE start_time <= ? AND status = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, OffsetDateTime.now());
        preparedStatement.setObject(2, 1);
        preparedStatement.executeUpdate();

        logger.info("Update Events Status to Expired");
    }

    public static void updateComingEvent() throws SQLException {
        String sql = "UPDATE events SET status = 1 WHERE start_time <= ? AND status = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, OffsetDateTime.now().plusMonths(6));
        preparedStatement.setObject(2, 2);
        preparedStatement.executeUpdate();

        logger.info("Update Events Status to Coming Soom");
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

    public static List<Event> loadByVenue(Venue venue) throws SQLException {
        int venueId = venue.getId();
        String sql = "SELECT * FROM events WHERE venue_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, venueId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static List<Event> loadByEventPlanner(EventPlanner e) throws SQLException {
        List<Event> events = new ArrayList<>();
        List<Integer> eventIds = PlannerEventMapper.loadEventIdsByPlanner(e);
        for(int eventId: eventIds)
            events.add(loadById(eventId));
        return events;
    }

    private static List<Event> load(ResultSet resultSet) throws SQLException {
        List<Event> events = new ArrayList<>();

        while (resultSet.next()) {
            int eventId = resultSet.getInt("id");
            String title = resultSet.getString("title").trim();
            String artist = resultSet.getString("artist").trim();
            int venueId = resultSet.getInt("venue_id");
            int status = resultSet.getInt("status");
            OffsetDateTime startTime = resultSet.getObject("start_time", OffsetDateTime.class);
            OffsetDateTime endTime = resultSet.getObject("end_time", OffsetDateTime.class);

            List<Section> sections = SectionMapper.loadSectionsByEventId(eventId);

            Venue venue = new Venue(venueId);

            Event event = new Event(eventId, sections, title, artist, venue, status, startTime, endTime);

            for (Section section : sections)
                section.setEvent(event);

            logger.info("Event Loaded [id=" + eventId + "]");

            events.add(event);
        }

        return events;
    }

    public static boolean timeCheck(Event event) throws SQLException {
        String sql = "SELECT * FROM events WHERE status = ? AND id <> ? AND" +
                "(( start_time <= ? AND end_time <= ? ) OR ( start_time <= ? AND end_time <= ? )) AND venue_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, event.getStatus());
        preparedStatement.setInt(2, event.getId());
        preparedStatement.setObject(3, event.getStartTime());
        preparedStatement.setObject(4, event.getStartTime());
        preparedStatement.setObject(5, event.getEndTime());
        preparedStatement.setObject(6, event.getEndTime());
        preparedStatement.setObject(7, event.getVenue().getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.isBeforeFirst();
    }

    public static void update(Event event) throws SQLException {
        String sql = "UPDATE events SET title = ?, artist = ?, venue_id = ?, status = ?, start_time = ?, end_time = ? WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getArtist());
        preparedStatement.setInt(3, event.getVenue().getId());
        preparedStatement.setInt(4, event.getStatus());
        preparedStatement.setObject(5, event.getStartTime());
        preparedStatement.setObject(6, event.getEndTime());
        preparedStatement.setInt(7, event.getId());
        preparedStatement.executeUpdate();

        for (Section section : event.getSections())
            SectionMapper.update(section);

        logger.info("Event Updated [id=" + event.getId() + "]");
    }

    public static void delete(Event event) throws SQLException {
        int eventId = event.getId();
        PlannerEventMapper.delete(eventId);
        String sql = "DELETE FROM events WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        preparedStatement.executeUpdate();
        logger.info("Existing Event Deleted [id=" + eventId + "]");
    }
}
