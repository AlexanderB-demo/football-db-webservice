package de.balogha.footballint.dbrestservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Goal {

    @Id
    @GeneratedValue
    private Long id;

    @Min(0)
    private int minute;

    @ManyToOne
    @NotNull
    @Valid
    private Player scorer;

    @ManyToOne
    @JoinColumn(name = "GAME_ID")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "TEAM_NAME")
    @NotNull
    @Valid
    private Team team;

    @Enumerated(EnumType.STRING)
    @NotNull
    private GoalType goalType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Player getScorer() {
        return scorer;
    }

    public void setScorer(Player scorer) {
        this.scorer = scorer;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", minute=" + minute +
                ", scorer=" + (scorer == null ? "null" : scorer.getId()) +
                ", game=" + (game == null ? "null" : game.getId()) +
                ", team=" + (team == null ? "null" : team.getId()) +
                ", goalType=" + goalType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goal goal = (Goal) o;

        if (minute != goal.minute) return false;
        if (scorer != null ? !scorer.equals(goal.scorer) : goal.scorer != null) return false;
        if (game != null ? !game.equals(goal.game) : goal.game != null) return false;
        if (team != null ? !team.equals(goal.team) : goal.team != null) return false;
        return goalType == goal.goalType;

    }

    @Override
    public int hashCode() {
        int result = minute;
        result = 31 * result + (scorer != null ? scorer.hashCode() : 0);
        result = 31 * result + (game != null ? game.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (goalType != null ? goalType.hashCode() : 0);
        return result;
    }
}
