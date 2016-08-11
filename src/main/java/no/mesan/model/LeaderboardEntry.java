package no.mesan.model;

public class LeaderboardEntry {
    private User user;
    private int totalConsumption;
    private double percentageOfAllConsumption;

    public LeaderboardEntry(User user, int totalConsumption, double percentageOfAllConsumption) {
        this.user = user;
        this.totalConsumption = totalConsumption;
        this.percentageOfAllConsumption = percentageOfAllConsumption;
    }

    public User getUser() {
        return user;
    }

    public int getTotalConsumption() {
        return totalConsumption;
    }

    public double getPercentageOfAllConsumption() {
        return percentageOfAllConsumption;
    }
}
