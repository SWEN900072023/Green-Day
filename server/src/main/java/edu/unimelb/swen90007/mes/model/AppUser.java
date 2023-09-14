package edu.unimelb.swen90007.mes.model;

import edu.unimelb.swen90007.mes.datamapper.AppUserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public abstract class AppUser {
    private static final Logger logger = LogManager.getLogger(AppUser.class);
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public AppUser(int id) {
        this.id = id;
    }

    public AppUser(int id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AppUser(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        if (email == null)
            load();
        return email;
    }

    public String getPassword() {
        if (password == null)
            load();
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        if (firstName == null)
            load();
        return firstName;
    }

    public String getLastName() {
        if (lastName == null)
            load();
        return lastName;
    }

    public void setUserDetail(String email, String password, String firstName, String lastName){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private void load() throws SQLException {
        logger.info("Loading User [id=" + id + "]");
        AppUser appUser;
        try {
            appUser = AppUserMapper.loadById(id);
            assert appUser != null;
            email = appUser.getEmail();
            password = appUser.getPassword();
            firstName = appUser.getFirstName();
            lastName = appUser.getLastName();
        } catch (SQLException e) {
            logger.error(String.format("Error loading User [id=%d]: %s", id, e.getMessage()));
        }
    }
}
