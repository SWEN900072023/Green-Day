package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.Lock.LockManager;
import edu.unimelb.swen90007.mes.exceptions.VersionUnmatchedException;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.Money;
import edu.unimelb.swen90007.mes.model.Section;
import edu.unimelb.swen90007.mes.model.SectionTickets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SectionMapper {
    private static final Logger logger = LogManager.getLogger(SectionMapper.class);

    public static void create(Section section, Connection connection) throws SQLException {
        String sql = "INSERT INTO sections (event_id, name, unit_price, currency, capacity, remaining_tickets, version_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, section.getEvent().getId());
        preparedStatement.setString(2, section.getName());
        preparedStatement.setBigDecimal(3, section.getMoney().getUnitPrice());
        preparedStatement.setString(4, section.getMoney().getCurrency());
        preparedStatement.setInt(5, section.getCapacity());
        preparedStatement.setInt(6, section.getCapacity());
        preparedStatement.setInt(7, 0);
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            int id = generatedKeys.getInt("id");
            section.setId(id);
        }
        logger.info("New Section Created [id=" + section.getId() + "]");
    }

    public static List<Section> loadSectionsByEventId(int eventId) throws SQLException {
        String sql = "SELECT * FROM sections WHERE event_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    public static Section loadById(int id) throws SQLException {
        String sql = "SELECT * FROM sections WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
    }

    public static Section loadSectionName(int id) throws SQLException {
        String sql = "SELECT name FROM sections WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String name = resultSet.getString("name").trim();
        return new Section(id, name);
    }

    public static Integer loadRemainingTickets(int sectionId) throws SQLException {
        String sql = "SELECT remaining_tickets FROM sections WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, sectionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("remaining_tickets");
    }

    private static int loadVersionNumber(int id, Connection connection) throws SQLException {
        String sql = "SELECT version_number FROM sections WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("version_number");
    }

    private static void versionIncrement(int sectionId, int versionNumber, Connection connection) throws SQLException {
        String sql = "UPDATE sections SET version_number = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, versionNumber);
        preparedStatement.setInt(2, sectionId);
        preparedStatement.executeUpdate();
        logger.info("Section Version Updated [id=" + sectionId + "]");
    }

    private static List<Section> load(ResultSet resultSet) throws SQLException {
        List<Section> sections = new ArrayList<>();

        while (resultSet.next()) {
            int sectionId = resultSet.getInt("id");
            int eventId = resultSet.getInt("event_id");
            String name = resultSet.getString("name").trim();
            BigDecimal unitPrice = resultSet.getBigDecimal("unit_price");
            String currency = resultSet.getString("currency").trim();
            int capacity = resultSet.getInt("capacity");
            int remainingTickets = resultSet.getInt("remaining_tickets");

            Event event = new Event(eventId);

            sections.add(new Section(sectionId, event, name, new Money(unitPrice, currency), capacity, remainingTickets));
            logger.info("Section Loaded [id=" + sectionId + "]");
        }

        return sections;
    }

    public static void update(Section section, Connection connection) throws SQLException, VersionUnmatchedException {
        int versionDirty = loadVersionNumber(section.getId(), connection);
        String sql = "UPDATE sections SET name = ?, unit_price = ?, currency = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, section.getName());
        preparedStatement.setBigDecimal(2, section.getMoney().getUnitPrice());
        preparedStatement.setString(3, section.getMoney().getCurrency());
        preparedStatement.setInt(4, section.getCapacity());
        preparedStatement.executeUpdate();
        int versionNew = loadVersionNumber(section.getId(), connection);
        if (versionDirty != versionNew)
            throw new VersionUnmatchedException();
        versionIncrement(section.getId(), versionNew + 1, connection);
        logger.info("Section Updated [id=" + section.getId() + "]");
    }

    public static void ticketsUpdate(SectionTickets sectionTickets, Connection connection) throws SQLException {
        String sql = "UPDATE sections SET remaining_tickets = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, sectionTickets.remainingTickets);
        preparedStatement.setInt(2, sectionTickets.sectionId);
        preparedStatement.executeUpdate();
        logger.info("Section Tickets Updated [id=" + sectionTickets.sectionId + "]");
    }

    public static void delete(Section section, Connection connection) throws SQLException {
        int id = section.getId();
        String sql = "DELETE FROM sections WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing Section Deleted [id=" + id + "]");
    }
}
