package de.balogha.footballint.dbrestservice.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Card {

    @Id
    @GeneratedValue
    private Long id;

    @Min(0)
    private int minute;

    @ManyToOne
    @NotNull
    @Valid
    private Player player;

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
    private CardType cardType;

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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", minute=" + minute +
                ", player=" + (player == null ? "null" : player.getId()) +
                ", game=" + (game == null ? "null" : game.getId()) +
                ", team=" + (team == null ? "null" : team.getId()) +
                ", cardType=" + cardType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (minute != card.minute) return false;
        if (player != null ? !player.equals(card.player) : card.player != null) return false;
        if (game != null ? !game.equals(card.game) : card.game != null) return false;
        if (team != null ? !team.equals(card.team) : card.team != null) return false;
        return cardType == card.cardType;

    }

    @Override
    public int hashCode() {
        int result = minute;
        result = 31 * result + (player != null ? player.hashCode() : 0);
        result = 31 * result + (game != null ? game.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (cardType != null ? cardType.hashCode() : 0);
        return result;
    }
}
