package no.mesan.dao.mappers;

import no.mesan.model.LeaderboardEntry;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaderboardMapper implements ResultSetMapper<LeaderboardEntry> {
    private UserMapper userMapper = new UserMapper("user_id");

    @Override
    public LeaderboardEntry map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new LeaderboardEntry(
                userMapper.map(i, resultSet, statementContext),
                resultSet.getInt("total_consumption"),
                resultSet.getDouble("percentage_of_all_consumption")
        );
    }
}
