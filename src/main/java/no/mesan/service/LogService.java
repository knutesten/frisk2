package no.mesan.service;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.LogDao;
import no.mesan.model.LogEntry;
import no.mesan.model.Type;
import no.mesan.model.User;

public class LogService {
    private LogDao logDao;

    public LogService(LogDao logDao) {
        this.logDao = logDao;
    }

    public ImmutableList<LogEntry> getLog() {
        return logDao.getLog(10, 0);
    }

    public int insert(User user, Type type) {
        return logDao.insert(user.getId(), type.getId());
    }
}
