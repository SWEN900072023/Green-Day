package edu.unimelb.swen90007.mes.scheduled.task;

import edu.unimelb.swen90007.mes.datamapper.EventMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.TimerTask;

public class EventUpdateTask extends TimerTask {
    private static final Logger logger = LogManager.getLogger(EventUpdateTask.class);
    @Override
    public void run() {
        try {
            EventMapper.updateEndedEvent();
            EventMapper.updateComingEvent();
        } catch (SQLException e) {
            logger.error("EventUpdateTask: " + e.getMessage());
        }
    }


}
