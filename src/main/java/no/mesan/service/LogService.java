package no.mesan.service;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.LogDao;
import no.mesan.model.LogEntry;
import no.mesan.model.Type;
import no.mesan.model.User;
import no.mesan.websocket.LogUpdate;

public class LogService {
    private LogDao logDao;

    public LogService(LogDao logDao) {
        this.logDao = logDao;
    }

    public ImmutableList<LogEntry> getLog() {
        return logDao.getLog(10, 0);
    }

    public int insert(User user, Type type) {
        int id = logDao.insert(user.getId(), type.getId());
        LogUpdate.updateClients();
        return id;
    }

    public void delete(User user) {
        if (logDao.undo(user.getId()) > 0)
            LogUpdate.updateClients();
    }

    public ImmutableList<LogEntry> getTodaysConsumption(User user) {
        return logDao.getTodaysConsumption(user.getId());
    }
}
