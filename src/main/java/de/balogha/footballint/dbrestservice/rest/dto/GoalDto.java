package de.balogha.footballint.dbrestservice.rest.dto;

import de.balogha.footballint.dbrestservice.model.GoalType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class GoalDto {

    @Min(0)
    private int minute;

    @NotNull
    private Long scorerId;

    @NotNull
    private Long teamId;

    @NotNull
    private GoalType goalType;

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Long getScorerId() {
        return scorerId;
    }

    public void setScorerId(Long scorerId) {
        this.scorerId = scorerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

}
