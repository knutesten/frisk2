package no.mesan.dao;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.mappers.LeaderboardMapper;
import no.mesan.model.LeaderboardEntry;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface LeaderboardDao {
    @RegisterMapper(LeaderboardMapper.class)
    @SqlQuery("SELECT user_id, last_name, first_name, username, email, total_consumption, (total_consumption * 100.0 / (SELECT SUM(amount) FROM log JOIN type ON type.id = type_id)) AS percentage_of_all_consumption " +
            "FROM \"user\" " +
            "JOIN (SELECT user_id, SUM(amount) AS total_consumption FROM log JOIN type ON type.id = type_id JOIN \"user\" ON \"user\".id = user_id GROUP BY user_id) AS user_sum " +
              "ON \"user\".id = user_id " +
            "ORDER BY total_consumption DESC LIMIT :limit")
    ImmutableList<LeaderboardEntry> getLeaderboard(@Bind("limit") int limit);
}
