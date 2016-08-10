package no.mesan.service;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.LogDao;
import no.mesan.model.LogEntry;

public class LogService {
    private LogDao logDao;

    public LogService(LogDao logDao) {
        this.logDao = logDao;
    }

    public ImmutableList<LogEntry> getLog() {
        return logDao.getLog();
    }
}
