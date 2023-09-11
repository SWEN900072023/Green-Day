package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.exceptions.UserNotFoundException;
import edu.unimelb.swen90007.mes.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data mapper that sits between the domain objects and the table users in the relational database.
 */
public final class UserMapper {
    private static final Logger logger = LogManager.getLogger(UserMapper.class);

    /**
     * Create an event planner or a customer.
     *
     * @param user a User object
     * @throws SQLException               if some error occurs while interacting with the database
     * @throws UserAlreadyExistsException if the user already exists
     */
    public static void create(User user) throws SQLException, UserAlreadyExistsException {
        String email = user.getEmail();
        if (doesUserExist(email))
            throw new UserAlreadyExistsException();

        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String type = user instanceof EventPlanner ? EventPlanner.class.getSimpleName() : Customer.class.getSimpleName();

        String sql = "INSERT INTO users (email, password, first_name, last_name, type) Values (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);
        preparedStatement.setString(5, type);
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next())
            user.setID(generatedKeys.getInt("id"));

        if (type.equalsIgnoreCase(EventPlanner.class.getSimpleName()))
            logger.info("New Event Planner Created [id=" + user.getID() + "]");
        else
            logger.info("New Customer Created [id=" + user.getID() + "]");
    }

    /**
     * Check if the user already exists.
     *
     * @param email the email received from the client request
     * @return a boolean indicating whether the user exists
     * @throws SQLException if some error occurs while interacting with the database
     */
    private static boolean doesUserExist(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.isBeforeFirst();
    }

    /**
     * Load all event planners and customers.
     *
     * @return the list of all event planners and customers
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static List<User> loadAll() throws SQLException {
        String sql = "SELECT * FROM users WHERE type != 'Administrator'";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        return load(resultSet);
    }

    /**
     * Load a user that may be the administrator, an event planner, or a customer.
     *
     * @param email the email received from the client request
     * @return a User object
     * @throws SQLException          if some error occurs while interacting with the database
     * @throws UserNotFoundException if the user does not exist
     */
    public static User loadByEmail(String email) throws SQLException, UserNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst())
            throw new UserNotFoundException();

        return load(resultSet).get(0);
    }

    /**
     * Load a user by ID.
     *
     * @param id the user ID
     * @return a User object
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static User loadByID(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
    }

    /**
     * Load a list of users given the ResultSet object.
     *
     * @param resultSet a ResultSet object
     * @return a list of users
     * @throws SQLException if some error occurs while interacting with the database
     */
    private static List<User> load(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String type = resultSet.getString("type");
            type = type.trim();

            if (type.equals(Administrator.class.getSimpleName()))
                users.add(new Administrator(id, email, password, firstName, lastName));
            else if (type.equals(EventPlanner.class.getSimpleName()))
                users.add(new EventPlanner(id, email, password, firstName, lastName));
            else if (type.equals(Customer.class.getSimpleName()))
                users.add(new Customer(id, email, password, firstName, lastName));

            logger.info("User Loaded [id=" + id + "]");
        }

        return users;
    }

    /**
     * Delete a user by ID.
     *
     * @param id the user ID
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing User Deleted [id=" + id + "]");
    }
}
