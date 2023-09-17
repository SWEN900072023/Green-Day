package edu.unimelb.swen90007.mes.model;

import com.alibaba.fastjson.annotation.JSONField;
import edu.unimelb.swen90007.mes.datamapper.AppUserMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public abstract class AppUser implements UserDetails {
    private static final Logger logger = LogManager.getLogger(AppUser.class);
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private final List<UserType> authorities = new ArrayList<>();

    public AppUser(Integer id) {
        this.id = id;
    }

    public AppUser(Integer id, String email, String password, String firstName, String lastName) {
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

    @Override
    public String getUsername() {
        return email;
    }

    public String loadEmail() {
        if (email == null)
            load();
        return email;
    }

    public String loadPassword() {
        if (password == null)
            load();
        return password;
    }

    public String loadFirstName() {
        if (firstName == null)
            load();
        return firstName;
    }

    public String loadLastName() {
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

    @Override @JSONField(serialize = false)
    public List<UserType> getAuthorities() {
        return authorities;
    }

    @Override @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }

    private void load() {
        logger.info("Loading User [id=" + id + "]");
        AppUser appUser;
        try {
            appUser = AppUserMapper.loadById(id);
            assert appUser != null;
            email = appUser.loadEmail();
            password = appUser.loadPassword();
            firstName = appUser.loadFirstName();
            lastName = appUser.loadLastName();
        } catch (SQLException e) {
            logger.error(String.format("Error loading User [id=%d]: %s", id, e.getMessage()));
        }
    }
}
