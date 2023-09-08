package edu.unimelb.swen90007.mes.datamapper;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.exceptions.UserNotFoundException;
import edu.unimelb.swen90007.mes.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * @param user a user object
     * @throws SQLException               if some error occurs while interacting with the database
     * @throws UserAlreadyExistsException if the user already exists
     */
    public static void createUser(User user) throws SQLException, UserAlreadyExistsException {
        String email = user.getEmail();
        if (doesUserExist(email))
            throw new UserAlreadyExistsException();

        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String type = user instanceof EventPlanner ? EventPlanner.class.getSimpleName() : Customer.class.getSimpleName();

        String sql = "INSERT INTO users (email, password, first_name, last_name, type) Values (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);
        preparedStatement.setString(5, type);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            user.setID(id);
            if (type.equalsIgnoreCase(EventPlanner.class.getSimpleName()))
                logger.info("New Event Planner Created [id=" + id + "]");
            else
                logger.info("New Customer Created [id=" + id + "]");
        }
    }

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
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE type != 'Administrator'";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String type = resultSet.getString("type");

            if (type.equalsIgnoreCase(EventPlanner.class.getSimpleName()))
                users.add(new EventPlanner(id, email, password, firstName, lastName));
            else if (type.equalsIgnoreCase(Customer.class.getSimpleName()))
                users.add(new Customer(id, email, password, firstName, lastName));
        }

        return users;
    }

    /**
     * Load a user that may be the administrator, an event planner, or a customer.
     *
     * @param email the email received from the client request
     * @return a user object
     * @throws SQLException          if some error occurs while interacting with the database
     * @throws UserNotFoundException if the user does not exist
     */
    public static User loadByEmail(String email) throws SQLException, UserNotFoundException {
        User user = null;

        String sql = "SELECT * FROM users WHERE email = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.isBeforeFirst())
            throw new UserNotFoundException();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String password = resultSet.getString("password");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String type = resultSet.getString("type");

            if (type.equalsIgnoreCase(Administrator.class.getSimpleName()))
                user = new Administrator(id, email, password, firstName, lastName);
            else if (type.equalsIgnoreCase(EventPlanner.class.getSimpleName()))
                user = new EventPlanner(id, email, password, firstName, lastName);
            else if (type.equalsIgnoreCase(Customer.class.getSimpleName()))
                user = new Customer(id, email, password, firstName, lastName);
        }

        return user;
    }
}
