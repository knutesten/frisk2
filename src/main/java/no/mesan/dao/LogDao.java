package no.mesan.dao;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.mappers.LogMapper;
import no.mesan.model.LogEntry;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface LogDao {
    @RegisterMapper(LogMapper.class)
    @SqlQuery("SELECT log.id as log_id, user_id, type_id, date, name, amount, first_name, last_name, username, email " +
              "FROM log " +
                "JOIN type " +
                  "ON log.type_id = type.id " +
                "JOIN \"user\" " +
                  "ON log.user_id = \"user\".id;")
    ImmutableList<LogEntry> getLog();
}

