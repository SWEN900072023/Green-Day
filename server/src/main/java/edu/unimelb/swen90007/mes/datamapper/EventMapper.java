package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
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

    public static void updateEndedEvent() throws SQLException {
        String sql = "UPDATE events SET status = 3 WHERE end_time <= ? AND status = 1";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, OffsetDateTime.now());
        preparedStatement.executeUpdate();

        logger.info("Update Events Status to Ended");
    }

    public static void updateComingEvent() throws SQLException {
        String sql = "UPDATE events SET status = 1 WHERE start_time <= ? AND status = 2";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, OffsetDateTime.now());
        preparedStatement.executeUpdate();

        logger.info("Update Events Status to Ended");
    }

    public static List<Event> loadAll() throws SQLException {
        String sql = "SELECT * FROM events WHERE status < 3 ORDER BY start_time";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return loadPartial(resultSet);
    }

    public static List<Event> loadNextSixMonths() throws SQLException {
        String sql = "SELECT * FROM events WHERE status = 1 ORDER BY start_time";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return loadPartial(resultSet);
    }

    public static List<Event> loadByPattern(String pattern) throws SQLException {
        pattern = "%" + pattern + "%";
        String sql = "SELECT * FROM events WHERE title LIKE ? AND status < 3 ORDER BY start_time";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, pattern);
        ResultSet resultSet = preparedStatement.executeQuery();
        return loadPartial(resultSet);
    }

    public static Event loadByIdAll(int eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
    }

    public static Event loadByIdPartial(int eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return loadPartial(resultSet).get(0);
    }

    public static List<Event> loadByVenue(Venue venue) throws SQLException {
        int venueId = venue.getId();
        String sql = "SELECT * FROM events WHERE venue_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, venueId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return loadPartial(resultSet);
    }

    public static List<Event> loadByEventPlanner(EventPlanner ep) throws SQLException {
        String sql = "SELECT * FROM events e, planner_events pe WHERE e.id = pe.event_id AND pe.planner_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, ep.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        return loadPartial(resultSet);
    }

    private static List<Event> load(ResultSet resultSet) throws SQLException {
        List<Event> events = new LinkedList<>();

        while (resultSet.next()) {
            int eventId = resultSet.getInt("id");
            String title = resultSet.getString("title").trim();
            String artist = resultSet.getString("artist").trim();
            int venueId = resultSet.getInt("venue_id");
            int status = resultSet.getInt("status");
            OffsetDateTime startTime = resultSet.getObject("start_time", OffsetDateTime.class);
            OffsetDateTime endTime = resultSet.getObject("end_time", OffsetDateTime.class);

            List<Section> sections = SectionMapper.loadSectionsByEventId(eventId);

            Venue venue = VenueMapper.loadById(venueId);

            Event event = new Event(eventId, sections, title, artist, venue, status, startTime, endTime);

            logger.info("Event Loaded [id=" + eventId + "]");

            events.add(event);
        }

        return events;
    }

    private static List<Event> loadPartial(ResultSet resultSet) throws SQLException {
        List<Event> events = new LinkedList<>();

        while (resultSet.next()) {
            int eventId = resultSet.getInt("id");
            String title = resultSet.getString("title").trim();
            String artist = resultSet.getString("artist").trim();
            int venueId = resultSet.getInt("venue_id");
            int status = resultSet.getInt("status");
            OffsetDateTime startTime = resultSet.getObject("start_time", OffsetDateTime.class).plusHours(10);
            OffsetDateTime endTime = resultSet.getObject("end_time", OffsetDateTime.class).plusHours(10);

            Venue venue = new Venue(venueId);

            Event event = new Event(eventId, title, artist, venue, status, startTime, endTime);

            logger.info("Event Partial Loaded [id=" + eventId + "]");

            events.add(event);
        }

        return events;
    }

    public static boolean doesTimeConflict(Event event) throws SQLException {
        String sql = "SELECT * FROM events WHERE status = ? AND id <> ? AND venue_id = ? AND" +
                "((start_time BETWEEN ? AND ?) OR ( end_time BETWEEN ? AND ?))";
        // event id is null if it is a create request
        if (event.getId() == null) event.setId(0);
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, event.getStatus());
        preparedStatement.setInt(2, event.getId());
        preparedStatement.setObject(3, event.getVenue().getId());
        preparedStatement.setObject(4, event.getStartTime());
        preparedStatement.setObject(5, event.getEndTime());
        preparedStatement.setObject(6, event.getStartTime());
        preparedStatement.setObject(7, event.getEndTime());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.isBeforeFirst();
    }

    public static void cancel(Event event) throws SQLException {
        List<Order> orders = OrderMapper.loadByEventId(event.getId());
        for(Order order : orders) {
            OrderMapper.cancel(order);
        }
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

        if (event.getStatus().equals(Constant.EVENT_CANCELLED))
            EventMapper.cancel(event);

        logger.info("Event Updated [id=" + event.getId() + "]");
    }

    public static void delete(Event event) throws SQLException {
        int eventId = event.getId();
        PlannerEventMapper.deleteByEvent(eventId);
        String sql = "DELETE FROM events WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        preparedStatement.executeUpdate();
        logger.info("Existing Event Deleted [id=" + eventId + "]");
    }
}
