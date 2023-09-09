package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Venue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Data mapper that sits between the domain objects and the table venues in the relational database.
 */
public final class VenueMapper {
    private static final Logger logger = LogManager.getLogger(VenueMapper.class);

    /**
     * Create a venue.
     *
     * @param venue a venue object
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static void create(Venue venue) throws SQLException {
        String sql = "INSERT INTO venues (name, address, capacity) VALUES (?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, venue.getName());
        preparedStatement.setString(2, venue.getAddress());
        preparedStatement.setInt(3, venue.getCapacity());
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        while (generatedKeys.next())
            venue.setID(generatedKeys.getInt("id"));

        logger.info("New Venue Created [id=" + venue.getID() + "]");
    }

    /**
     * Load a venue by ID.
     *
     * @param id the venue ID
     * @return a venue object
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static Venue loadByID(int id) throws SQLException {
        Venue venue = null;

        String sql = "SELECT * FROM venues WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            int capacity = resultSet.getInt("capacity");

            venue = new Venue(id, name, address, capacity);
            logger.info("Venue Loaded [id=" + id + "]");
        }

        return venue;
    }

    /**
     * Delete a venue by ID.
     *
     * @param id the venue ID
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM venues WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing Venue Deleted [id=" + id + "]");
    }
}
