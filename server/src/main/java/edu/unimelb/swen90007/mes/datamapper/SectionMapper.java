package edu.unimelb.swen90007.mes.datamapper;

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
        preparedStatement.setInt(1, section.getEvent().getID());
        preparedStatement.setString(2, section.getName());
        preparedStatement.setBigDecimal(3, section.getMoney().getUnitPrice());
        preparedStatement.setString(4, section.getMoney().getCurrency());
        preparedStatement.setInt(5, section.getCapacity());
        preparedStatement.setInt(6, section.getRemainingTickets());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            section.setID(generatedKeys.getInt("id"));
        logger.info("New Section Created [id=" + section.getID() + "]");
    }

    public static List<Section> loadByEventID(int eventID) throws SQLException {
        List<Section> sections = new ArrayList<>();

        String sql = "SELECT * FROM sections WHERE event_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventID);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name").trim();
            BigDecimal unitPrice = resultSet.getBigDecimal("unit_price");
            String currency = resultSet.getString("currency").trim();
            int capacity = resultSet.getInt("capacity");
            int remainingTickets = resultSet.getInt("remaining_tickets");

            sections.add(new Section(id, null, name, new Money(unitPrice, currency), capacity, remainingTickets));
            logger.info("Section Loaded [id=" + id + "]");
        }

        return sections;
    }

    public static void update(Section section) {

    }

    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM sections WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing Section Deleted [id=" + id + "]");
    }
}
