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
public final class AppUserMapper {
    private static final Logger logger = LogManager.getLogger(AppUserMapper.class);

    /**
     * Create an event planner or a customer.
     *
     * @param user a user object
     * @throws SQLException               if some error occurs while interacting with the database
     * @throws UserAlreadyExistsException if the user already exists
     */
    public static void create(AppUser user) throws SQLException, UserAlreadyExistsException {
        String email = user.getEmail();
        if (doesUserExist(email))
            throw new UserAlreadyExistsException();

        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String type;
        if (user instanceof Administrator) {
            type = Administrator.class.getSimpleName();
        } else if (user instanceof EventPlanner) {
            type = EventPlanner.class.getSimpleName();
        } else {
            type = Customer.class.getSimpleName();
        }

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
            user.setId(generatedKeys.getInt("id"));

        if (type.equalsIgnoreCase(Administrator.class.getSimpleName())) {
            logger.info("New Administrator Created [id=" + user.getId() + "]");
        } else if (type.equalsIgnoreCase(EventPlanner.class.getSimpleName())) {
            logger.info("New Event Planner Created [id=" + user.getId() + "]");
        } else {
            logger.info("New Customer Created [id=" + user.getId() + "]");
        }
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
     * Verify user's email and password.
     *
     * @param user an AppUser object
     * @return a boolean indicating whether the user authentication was successful
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static String userAuthentication(AppUser user) throws SQLException {
        String sql = "SELECT type FROM users WHERE email = ? AND password = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.isBeforeFirst()) {
            resultSet.next();
            return resultSet.getString("type");
        }
        else {
            return  null;
        }
    }

    /**
     * Load all event planners and customers.
     *
     * @return the list of all event planners and customers
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static List<AppUser> loadAll() throws SQLException {
        String sql = "SELECT * FROM users WHERE type != 'Administrator'";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        return load(resultSet);
    }

    /**
     * Load all customers.
     *
     * @return the list of all customers
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static List<AppUser> loadAllCustomer() throws SQLException {
        String sql = "SELECT * FROM users WHERE type = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, Customer.class.getSimpleName());
        ResultSet resultSet = preparedStatement.executeQuery();

        return load(resultSet);
    }

    /**
     * Load all event planners.
     *
     * @return the list of all event planners
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static List<AppUser> loadAllEventPlanners() throws SQLException {
        String sql = "SELECT * FROM users WHERE type = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, EventPlanner.class.getSimpleName());
        ResultSet resultSet = preparedStatement.executeQuery();

        return load(resultSet);
    }

    /**
     * Load all uninvited event planners.
     *
     * @return the list of all uninvited event planners
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static List<AppUser> loadUninvitedEventPlanners(Event event) throws SQLException {
        String sql = "SELECT planner_id FROM planner_events WHERE event_id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, event.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Integer> invitedPlannerIds = new ArrayList<>();
        while (resultSet.next()) {
            int pid = resultSet.getInt("planner_id");
            invitedPlannerIds.add(pid);
        }
        sql = "SELECT * FROM users WHERE type = ? AND id <> ?";
        for(int i=1; i<invitedPlannerIds.size(); i++) {
            sql += " AND id <> ?";
        }

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, EventPlanner.class.getSimpleName());
        for(int i=0; i<invitedPlannerIds.size(); i++) {
            preparedStatement.setInt(i+2, invitedPlannerIds.get(i));
        }
        resultSet = preparedStatement.executeQuery();

        return load(resultSet);
    }

    /**
     * Load a user that may be the administrator, an event planner, or a customer.
     *
     * @param email the email received from the client request
     * @return an AppUser object
     * @throws SQLException          if some error occurs while interacting with the database
     * @throws UserNotFoundException if the user does not exist
     */
    public static AppUser loadByEmail(String email) throws SQLException, UserNotFoundException {
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
     * @return an AppUser object
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static AppUser loadById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return load(resultSet).get(0);
    }

    public static AppUser loadByIdPartial(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return loadPartial(resultSet).get(0);
    }

    /**
     * Load a list of users given the ResultSet object.
     *
     * @param resultSet a ResultSet object
     * @return a list of users
     * @throws SQLException if some error occurs while interacting with the database
     */
    private static List<AppUser> load(ResultSet resultSet) throws SQLException {
        List<AppUser> users = new ArrayList<>();

        while (resultSet.next()) {
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
     * Load a list of users(with partial information) given the ResultSet object.
     *
     * @param resultSet a ResultSet object
     * @return a list of users
     * @throws SQLException if some error occurs while interacting with the database
     */
    private static List<AppUser> loadPartial(ResultSet resultSet) throws SQLException {
        List<AppUser> users = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String type = resultSet.getString("type");
            type = type.trim();

            AppUser user;
            if (type.equals(Administrator.class.getSimpleName()))
                user = new Administrator(id);
            else if (type.equals(EventPlanner.class.getSimpleName()))
                user = new EventPlanner(id);
            else
                user = new Customer(id);

            user.setFirstName(firstName);
            user.setLastName(lastName);

            users.add(user);

            logger.info("User Loaded [id=" + id + "]");
        }

        return users;
    }

    /**
     * Update a user by ID.
     *
     * @param user an AppUser object
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static void update(AppUser user) throws SQLException {
        String sql = "UPDATE users SET email = ?, password = ?, first_name = ?, last_name = ? WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getFirstName());
        preparedStatement.setString(4, user.getLastName());
        preparedStatement.setInt(5, user.getId());
        preparedStatement.executeUpdate();
        logger.info("User Updated [id=" + user.getId() + "]");
    }

    /**
     * Delete a user by ID.
     *
     * @param user an AppUser object
     * @throws SQLException if some error occurs while interacting with the database
     */
    public static void delete(AppUser user) throws SQLException {
        int id = user.getId();
        String sql = "DELETE FROM users WHERE id = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        logger.info("Existing User Deleted [id=" + id + "]");
    }
}
