package de.balogha.footballint.dbrestservice.rest.dto;

import de.balogha.footballint.dbrestservice.model.CardType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CardDto {

    @Min(0)
    private int minute;

    @NotNull
    private Long playerId;

    @NotNull
    private Long teamId;

    @NotNull
    private CardType cardType;

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

}
