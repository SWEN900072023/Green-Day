package edu.unimelb.swen90007.mes.service;

import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.sql.SQLException;

public interface IAppUserService extends UserDetailsService {
    void register(AppUser user) throws SQLException, UserAlreadyExistsException;
}
