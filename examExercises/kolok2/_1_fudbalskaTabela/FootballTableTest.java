package kolok2._1_fudbalskaTabela;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

class Team implements Comparable<Team> {
    private final String name;

    int goals;
    private int wins;
    private int losses;
    private int draws;

    public Team(String name) {
        this.name = name;
        this.goals = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public void addGoalDifference(int goals) {
        this.goals += goals;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public void addDraw() {
        draws++;
    }

    public String getName() {
        return name;
    }

    public int getGoalsDifference() {
        return goals;
    }

    public int getTotalGamesPlayed() {
        return draws + wins + losses;
    }

    public int getTotalPoints() {
        return wins * 3 + draws;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, getTotalGamesPlayed(), wins, draws, losses, getTotalPoints());
    }

    @Override
    public int compareTo(Team o) {
        Comparator<Team> comparator = Comparator.comparing(Team::getTotalPoints)
                .thenComparing(Team::getGoalsDifference)
                .thenComparing(Comparator.comparing(Team::getName).reversed());
        return comparator.compare(this, o);
    }
}

class FootballTable {

    HashMap<String, Team> teamsByName;

    public FootballTable() {
        this.teamsByName = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        updateTeamStats(homeTeam, homeGoals - awayGoals, Integer.compare(homeGoals, awayGoals));
        updateTeamStats(awayTeam, awayGoals - homeGoals, Integer.compare(awayGoals, homeGoals));
    }

    private void updateTeamStats(String name, int goals, int result) {
        Team team = new Team(name);
        if (teamsByName.containsKey(name)) {
            team = teamsByName.get(name);
        }
        team.addGoalDifference(goals);
        if (result < 0) team.addLoss();
        else if (result > 0) team.addWin();
        else team.addDraw();
        teamsByName.put(name, team);
    }

    public void printTable() {
        AtomicInteger counter = new AtomicInteger();
        teamsByName.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entrySet -> System.out.println(String.format("%2d. ", counter.incrementAndGet()) + entrySet.getValue()));
    }
}