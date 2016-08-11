package no.mesan.dao.mappers;

import no.mesan.model.LogEntry;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogMapper implements ResultSetMapper<LogEntry>{
    private final UserMapper userMapper = new UserMapper("user_id");
    private final TypeMapper typeMapper = new TypeMapper("type_id");

    @Override
    public LogEntry map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new LogEntry(
                resultSet.getInt("log_id"),
                resultSet.getTimestamp("date").toInstant(),
                userMapper.map(i, resultSet, statementContext),
                typeMapper.map(i, resultSet, statementContext)
        );
    }
}
