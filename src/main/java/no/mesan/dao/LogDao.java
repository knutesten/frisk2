package no.mesan.dao;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.mappers.LogMapper;
import no.mesan.model.LogEntry;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface LogDao {
    @RegisterMapper(LogMapper.class)
    @SqlQuery("SELECT log.id as log_id, user_id, type_id, date, name, amount, first_name, last_name, username, email " +
              "FROM log " +
                "JOIN type " +
                  "ON log.type_id = type.id " +
                "JOIN \"user\" " +
                  "ON log.user_id = \"user\".id " +
              "ORDER BY date DESC " +
              "LIMIT :limit OFFSET :offset")
    ImmutableList<LogEntry> getLog(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlUpdate("INSERT INTO log (date, user_id, type_id) VALUES (NOW(), :userId, :typeId)")
    int insert(@Bind("userId") int userId, @Bind("typeId") int typeId);

    @SqlUpdate("DELETE FROM log WHERE id = (SELECT id FROM log WHERE user_id = :userId AND date >= (NOW() - INTERVAL '5' MINUTE) ORDER BY date DESC LIMIT 1)")
    int undo(@Bind("userId") int userId);

    @RegisterMapper(LogMapper.class)
    @SqlQuery("SELECT log.id as log_id, user_id, type_id, date, name, amount, first_name, last_name, username, email " +
              "FROM log " +
                "JOIN type " +
                  "ON log.type_id = type.id " +
                "JOIN \"user\" " +
                  "ON log.user_id = \"user\".id " +
              "WHERE date >= CURRENT_DATE AND user_id = :userId " +
              "ORDER BY date ASC ")
    ImmutableList<LogEntry> getTodaysConsumption(@Bind("userId") int userId);

    @SqlQuery("SELECT SUM(amount) FROM log JOIN type ON type_Id = type.id")
    int getTotalConsumption();
}

