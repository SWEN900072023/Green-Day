package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.AppUserMapper;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.service.IAppUserService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

public class AppUserService implements IAppUserService {
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user;
        try {
            user = AppUserMapper.loadByEmail(email);
        } catch (Exception e) {
            throw new UsernameNotFoundException(email);
        }

        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        User.UserBuilder userBuilder = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getClass().getSimpleName());

        return userBuilder.build();
    }

    public void register(AppUser user) throws SQLException, UserAlreadyExistsException {
        UnitOfWork.getInstance().registerNew(user);
        // encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // create user
        UnitOfWork.getInstance().commit();
    }
}
