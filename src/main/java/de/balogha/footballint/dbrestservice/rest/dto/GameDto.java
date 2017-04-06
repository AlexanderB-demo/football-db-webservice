package de.balogha.footballint.dbrestservice.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameDto {

    private Long id;

    @Min(0)
    private Integer numberOfSpectators;

    @NotNull
    @Min(0)
    private Integer round;

    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private Date kickoffDatetime;

    @NotNull
    private Long competitionId;

    @NotNull
    private Long homeTeamId;

    @NotNull
    private Long awayTeamId;

    @Valid
    @NotNull
    private List<GoalDto> goals = new ArrayList<>();

    @Valid
    @NotNull
    private List<CardDto> cards = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberOfSpectators() {
        return numberOfSpectators;
    }

    public void setNumberOfSpectators(Integer numberOfSpectators) {
        this.numberOfSpectators = numberOfSpectators;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Date getKickoffDatetime() {
        return kickoffDatetime;
    }

    public void setKickoffDatetime(Date kickoffDatetime) {
        this.kickoffDatetime = kickoffDatetime;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public List<GoalDto> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalDto> goals) {
        this.goals = goals;
    }

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
    }
}
