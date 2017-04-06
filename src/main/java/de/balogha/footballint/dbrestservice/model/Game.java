package de.balogha.footballint.dbrestservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @Min(0)
    private Integer numberOfSpectators;

    @Min(0)
    private int round;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date kickoffDatetime;

    @ManyToOne
    @NotNull
    @Valid
    private Competition competition;

    @ManyToOne
    @NotNull
    @Valid
    private Team homeTeam;

    @ManyToOne
    @NotNull
    @Valid
    private Team awayTeam;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @Valid
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @Valid
    private List<Card> cards = new ArrayList<>();

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

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Date getKickoffDatetime() {
        return kickoffDatetime;
    }

    public void setKickoffDatetime(Date kickoffDatetime) {
        this.kickoffDatetime = kickoffDatetime;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        card.setGame(this);
        cards.add(card);
    }

    public void addGoal(Goal goal) {
        goal.setGame(this);
        goals.add(goal);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", numberOfSpectators=" + numberOfSpectators +
                ", round=" + round +
                ", kickoffDatetime=" + kickoffDatetime +
                ", competition=" + competition +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", goals=" + goals +
                ", cards=" + cards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (kickoffDatetime != null ? !kickoffDatetime.equals(game.kickoffDatetime) : game.kickoffDatetime != null)
            return false;
        if (homeTeam != null ? !homeTeam.equals(game.homeTeam) : game.homeTeam != null) return false;
        return awayTeam != null ? awayTeam.equals(game.awayTeam) : game.awayTeam == null;

    }

    @Override
    public int hashCode() {
        int result = kickoffDatetime != null ? kickoffDatetime.hashCode() : 0;
        result = 31 * result + (homeTeam != null ? homeTeam.hashCode() : 0);
        result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
        return result;
    }
}
