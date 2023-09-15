package edu.unimelb.swen90007.mes.service.impl;

import edu.unimelb.swen90007.mes.datamapper.AppUserMapper;
import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.AppUser;
import edu.unimelb.swen90007.mes.model.Event;
import edu.unimelb.swen90007.mes.service.PublicServiceInterface;
import edu.unimelb.swen90007.mes.util.UnitOfWork;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.List;

public class PublicService implements PublicServiceInterface {
    @Override
    public List<Event> viewAllEvents() throws SQLException {
        return EventMapper.loadAll();
    }

    @Override
    public List<Event> viewNextSixMothsEvents() throws SQLException {
        return EventMapper.loadNextSixMonths();
    }

    @Override
    public List<Event> searchEvents(String pattern) throws SQLException {
        return EventMapper.loadByPattern(pattern);
    }

    @Override
    public void register(AppUser user) throws SQLException, UserAlreadyExistsException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        UnitOfWork.getInstance().registerNew(user);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public void modifyUser(AppUser user) throws SQLException, UserAlreadyExistsException {
        UnitOfWork.getInstance().registerDirty(user);
        UnitOfWork.getInstance().commit();
    }

    @Override
    public String userAuthentication(AppUser user) throws SQLException {
        return AppUserMapper.userAuthentication(user);
    }
}
