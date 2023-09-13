package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.model.Money;
import edu.unimelb.swen90007.mes.model.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SectionMapper {
    private static final Logger logger = LogManager.getLogger(SectionMapper.class);

    public static void create(Section section) throws SQLException {
        String sql = "INSERT INTO sections (event_id, name, unit_price, currency, capacity, remaining_tickets) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, section.getEvent().getId());
        preparedStatement.setString(2, section.getName());
        preparedStatement.setBigDecimal(3, section.getMoney().getUnitPrice());
        preparedStatement.setString(4, section.getMoney().getCurrency());
        preparedStatement.setInt(5, section.getCapacity());
        preparedStatement.setInt(6, section.getRemainingTickets());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            section.setId(generatedKeys.getInt("id"));
        logger.info("New Section Created [id=" + section.getId() + "]");
    }

    public static List<Section> loadSectionsByEventId(int eventId) throws SQLException {
        List<Section> sections = new ArrayList<>();

        String sql = "SELECT id FROM sections WHERE event_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int sectionId = resultSet.getInt("id");
            sections.add(new Section(sectionId));
        }

        return sections;
    }

    public static Section loadById(int id) throws SQLException {
        String sql = "SELECT * FROM sections WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
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

    public static void update(Section section) throws SQLException {
        String sql = "UPDATE sections SET name = ?, unit_price = ?, currency = ?, capacity = ?, remaining_tickets = ? WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, section.getName());
        preparedStatement.setBigDecimal(2, section.getMoney().getUnitPrice());
        preparedStatement.setString(3, section.getMoney().getCurrency());
        preparedStatement.setInt(4, section.getCapacity());
        preparedStatement.setInt(5, section.getRemainingTickets());
        preparedStatement.setInt(6, section.getId());
        preparedStatement.executeUpdate();
        logger.info("Section Updated [id=" + section.getId() + "]");
    }

    public static void increaseRemainingTickets(int id, int quantity) throws SQLException {
        String sql = "UPDATE sections SET remaining_tickets = remaining_tickets + ? WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, quantity);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
        logger.info("Remaining Tickets Increased By " + quantity + " [id=" + id + "]");
    }

    public static void decreaseRemainingTickets(int id, int quantity) throws SQLException {
        String sql = "UPDATE sections SET remaining_tickets = remaining_tickets - ? WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, quantity);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
        logger.info("Remaining Tickets Decreased By " + quantity + " [id=" + id + "]");
    }

    public static void delete(Section section) throws SQLException {
        int id = section.getId();
        String sql = "DELETE FROM sections WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing Section Deleted [id=" + id + "]");
    }
}
