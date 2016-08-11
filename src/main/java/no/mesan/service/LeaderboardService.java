package no.mesan.service;

import com.google.common.collect.ImmutableList;
import no.mesan.dao.LeaderboardDao;
import no.mesan.model.LeaderboardEntry;

public class LeaderboardService {
    private LeaderboardDao leaderboardDao;

    public LeaderboardService(LeaderboardDao leaderboardDao) {
        this.leaderboardDao = leaderboardDao;
    }

    public ImmutableList<LeaderboardEntry> getLeaderboard() {
        return leaderboardDao.getLeaderboard(10);
    }
}
