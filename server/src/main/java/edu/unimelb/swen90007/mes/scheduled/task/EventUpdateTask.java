package edu.unimelb.swen90007.mes.scheduled.task;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import edu.unimelb.swen90007.mes.model.Event;

import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

public class EventUpdateTask extends TimerTask {
    @Override
    public void run() {
        try {
            List<Event> events = EventMapper.loadExpiredEvent();
            for(Event e : events)
                EventMapper.moveToEventExpired(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
