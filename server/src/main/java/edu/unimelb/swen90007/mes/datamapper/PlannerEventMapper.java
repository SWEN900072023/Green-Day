package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.EventPlanner;
import edu.unimelb.swen90007.mes.model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PlannerEventMapper {
    private static final Logger logger = LogManager.getLogger(PlannerEventMapper.class);

    public static void create(int eventId, int plannerId) throws SQLException {
        String sql = "INSERT INTO planner_events (event_id, planner_id) VALUES (?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        preparedStatement.setInt(2, plannerId);
        preparedStatement.executeUpdate();
        logger.info("New Association Created [event_id=" + eventId + "], [planner_id=" + plannerId + "]");
    }

    public static void inviteEventPlanner(EventPlanner ep, Event event) throws SQLException {
        int planner_id = ep.getId();
        int event_id = event.getId();
        String sql = "INSERT INTO planner_events (planner_id, event_id) Values (?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, planner_id);
        preparedStatement.setInt(2, event_id);
        preparedStatement.executeUpdate();
        logger.info("Invite EventPlanner [id=" + planner_id + "]");
    }

    public static void deleteByEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM planner_events WHERE event_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        preparedStatement.executeUpdate();
        logger.info("Association Deleted [event_id=" + eventId + "]");
    }

    public static void deleteByEventPlanner(int plannerId) throws SQLException {
        String sql = "DELETE FROM planner_events WHERE planner_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, plannerId);
        preparedStatement.executeUpdate();
        logger.info("Association Deleted [event_id=" + plannerId + "]");
    }

    public static boolean checkRelation(EventPlanner ep, Event event) throws SQLException {
        String sql = "SELECT * FROM planner_events WHERE planner_id = ? AND event_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, ep.getId());
        preparedStatement.setInt(2, event.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.isBeforeFirst();
    }
}
