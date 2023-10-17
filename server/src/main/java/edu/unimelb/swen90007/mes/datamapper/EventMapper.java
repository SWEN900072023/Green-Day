package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.Lock.LockManager;
import edu.unimelb.swen90007.mes.constants.Constant;
import edu.unimelb.swen90007.mes.exceptions.TimeConflictException;
import edu.unimelb.swen90007.mes.exceptions.VersionUnmatchedException;
import edu.unimelb.swen90007.mes.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

public final class EventMapper {
    private static final Logger logger = LogManager.getLogger(EventMapper.class);

    public static void create(Event event, Connection connection) throws SQLException, TimeConflictException {
        String sql = "INSERT INTO events (title, artist, venue_id, status, start_time, end_time, version_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getArtist());
        preparedStatement.setInt(3, event.getVenue().getId());
        preparedStatement.setInt(4, event.getStatus());
        preparedStatement.setObject(5, event.getStartTime());
        preparedStatement.setObject(6, event.getEndTime());
        preparedStatement.setObject(7, 0);
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            event.setId(generatedKeys.getInt("id"));
        logger.info("New Event Created [id=" + event.getId() + "]");
        PlannerEventMapper.create(event.getId(), event.getFirstPlannerId());
        LockManager.getInstance().validationLock.lock();
        try{
            if (doesTimeConflict(event))
                throw new TimeConflictException();
            connection.commit();
        } finally {
            LockManager.getInstance().validationLock.unlock();
        }
    }

    public static void updateEndedEvent() throws SQLException {
        String sql = "UPDATE events SET status = 3 WHERE end_time <= ? AND status = 1";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, LocalDateTime.now());
        preparedStatement.executeUpdate();

        logger.info("Update Events Status to Ended");
    }

    public static void updateComingEvent() throws SQLException {
        String sql = "UPDATE events SET status = 1 WHERE start_time <= ? AND status = 2";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, LocalDateTime.now());
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

    private static int loadVersionNumber(int id, Connection connection) throws SQLException {
        String sql = "SELECT version_number FROM events WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("version_number");
    }

    private static void versionIncrement(int eventId, int versionNumber, Connection connection) throws SQLException {
        String sql = "UPDATE events SET version_number = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, versionNumber);
        preparedStatement.setInt(2, eventId);
        preparedStatement.executeUpdate();
        logger.info("Event Version Updated [id=" + eventId + "]");
    }

    private static List<Event> load(ResultSet resultSet) throws SQLException {
        List<Event> events = new LinkedList<>();

        while (resultSet.next()) {
            int eventId = resultSet.getInt("id");
            String title = resultSet.getString("title").trim();
            String artist = resultSet.getString("artist").trim();
            int venueId = resultSet.getInt("venue_id");
            int status = resultSet.getInt("status");
            LocalDateTime startTime = resultSet.getObject("start_time", LocalDateTime.class);
            LocalDateTime endTime = resultSet.getObject("end_time", LocalDateTime.class);

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
            LocalDateTime startTime = resultSet.getObject("start_time", LocalDateTime.class);
            LocalDateTime endTime = resultSet.getObject("end_time", LocalDateTime.class);

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

    public static void update(Event event, Connection connection) throws SQLException, VersionUnmatchedException, TimeConflictException {
        int versionDirty = loadVersionNumber(event.getId(), connection);
        System.out.println(event.getArtist());
        String sql = "UPDATE events SET title = ?, artist = ?, venue_id = ?, status = ?, start_time = ?, end_time = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, event.getTitle());
        preparedStatement.setString(2, event.getArtist());
        preparedStatement.setInt(3, event.getVenue().getId());
        preparedStatement.setInt(4, event.getStatus());
        preparedStatement.setObject(5, event.getStartTime());
        preparedStatement.setObject(6, event.getEndTime());
        preparedStatement.setInt(7, event.getId());
        preparedStatement.executeUpdate();

        int versionNew = loadVersionNumber(event.getId(), connection);
        if (versionDirty != versionNew)
            throw new VersionUnmatchedException();
        versionIncrement(event.getId(), versionNew + 1, connection);
        if (doesTimeConflict(event))
            throw new TimeConflictException();
        logger.info("Event Updated [id=" + event.getId() + "]");
    }

    public static void delete(Event event, Connection connection) throws SQLException {
        int eventId = event.getId();
        PlannerEventMapper.deleteByEvent(eventId);
        String sql = "DELETE FROM events WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        preparedStatement.executeUpdate();
        logger.info("Existing Event Deleted [id=" + eventId + "]");
    }
}
