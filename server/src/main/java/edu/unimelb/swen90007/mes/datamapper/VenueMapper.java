package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.model.Venue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
     * Load all venues.
     *
     * @return the list of all venues
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static List<Venue> loadAll() throws SQLException {
        String sql = "SELECT * FROM venues";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet);
    }

    /**
     * Load a venue by ID.
     *
     * @param id the venue ID
     * @return a venue object
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static Venue loadByID(int id) throws SQLException {
        String sql = "SELECT * FROM venues WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
    }

    /**
     * Load a list of venues given the ResultSet object.
     *
     * @param resultSet a ResultSet object
     * @return a list of venues
     * @throws SQLException if some error occurs while interacting with the database
     */
    private static List<Venue> load(ResultSet resultSet) throws SQLException {
        List<Venue> venues = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            int capacity = resultSet.getInt("capacity");

            venues.add(new Venue(id, name, address, capacity));
            logger.info("Venue Loaded [id=" + id + "]");
        }

        return venues;
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
