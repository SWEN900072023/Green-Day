package edu.unimelb.swen90007.mes.scheduled.task;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;

import java.sql.SQLException;
import java.util.TimerTask;

public class EventUpdateTask extends TimerTask {
    @Override
    public void run() {
        try {
            EventMapper.updateEndedEvent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
