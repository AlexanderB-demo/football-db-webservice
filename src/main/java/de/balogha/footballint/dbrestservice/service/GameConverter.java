package de.balogha.footballint.dbrestservice.service;

import de.balogha.footballint.dbrestservice.model.Card;
import de.balogha.footballint.dbrestservice.model.Game;
import de.balogha.footballint.dbrestservice.model.Goal;
import de.balogha.footballint.dbrestservice.repository.CompetitionRepository;
import de.balogha.footballint.dbrestservice.repository.PlayerRepository;
import de.balogha.footballint.dbrestservice.repository.TeamRepository;
import de.balogha.footballint.dbrestservice.rest.dto.CardDto;
import de.balogha.footballint.dbrestservice.rest.dto.GameDto;
import de.balogha.footballint.dbrestservice.rest.dto.GoalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameConverter {

    private CompetitionRepository competitionRepository;
    private TeamRepository teamRepository;
    private PlayerRepository playerRepository;

    @Autowired
    public GameConverter(CompetitionRepository competitionRepository, TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.competitionRepository = competitionRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    public Game gameDtoToGame(GameDto compactGame) {
        Game game = new Game();
        game.setId(compactGame.getId());
        game.setKickoffDatetime(compactGame.getKickoffDatetime());
        game.setRound(compactGame.getRound());
        game.setNumberOfSpectators(compactGame.getNumberOfSpectators());
        game.setCompetition(competitionRepository.findOne(compactGame.getCompetitionId()));
        game.setHomeTeam(teamRepository.findOne(compactGame.getHomeTeamId()));
        game.setAwayTeam(teamRepository.findOne(compactGame.getAwayTeamId()));
        game.setCards(compactGame.getCards()
                .stream()
                .map(compactCard -> cardDtoToCard(compactCard, game))
                .collect(Collectors.toList()));
        game.setGoals(compactGame.getGoals()
                .stream()
                .map(compactGoal -> goalDtoToGoal(compactGoal, game))
                .collect(Collectors.toList()));
        return game;
    }

    public List<Game> gameDtostoGames(List<GameDto> compactGames) {
        return compactGames.stream()
                .map(this::gameDtoToGame)
                .collect(Collectors.toList());
    }

    public GameDto gameToGameDto(Game game) {
        GameDto compactGame = new GameDto();
        compactGame.setId(game.getId());
        compactGame.setKickoffDatetime(game.getKickoffDatetime());
        compactGame.setRound(game.getRound());
        compactGame.setNumberOfSpectators(game.getNumberOfSpectators());
        compactGame.setCompetitionId(game.getCompetition().getId());
        compactGame.setHomeTeamId(game.getHomeTeam().getId());
        compactGame.setAwayTeamId(game.getAwayTeam().getId());
        compactGame.setCards(game.getCards()
                .stream()
                .map(this::cardToCardDto)
                .collect(Collectors.toList()));
        compactGame.setGoals(game.getGoals()
                .stream()
                .map(this::goalToGoalDto)
                .collect(Collectors.toList()));
        return compactGame;
    }

    public List<GameDto> gamesToGameDtos(List<Game> games) {
        return games.stream()
                .map(this::gameToGameDto)
                .collect(Collectors.toList());
    }

    private Card cardDtoToCard(CardDto compactCard, Game parent) {
        Card card = new Card();
        card.setCardType(compactCard.getCardType());
        card.setMinute(compactCard.getMinute());
        card.setPlayer(playerRepository.findOne(compactCard.getPlayerId()));
        card.setTeam(teamRepository.findOne(compactCard.getTeamId()));
        card.setGame(parent);
        return card;
    }

    private Goal goalDtoToGoal(GoalDto compactGoal, Game parent) {
        Goal goal = new Goal();
        goal.setGoalType(compactGoal.getGoalType());
        goal.setMinute(compactGoal.getMinute());
        goal.setScorer(playerRepository.findOne(compactGoal.getScorerId()));
        goal.setTeam(teamRepository.findOne(compactGoal.getTeamId()));
        goal.setGame(parent);
        return goal;
    }

    private CardDto cardToCardDto(Card card) {
        CardDto compactCard = new CardDto();
        compactCard.setCardType(card.getCardType());
        compactCard.setMinute(card.getMinute());
        compactCard.setPlayerId(card.getPlayer().getId());
        compactCard.setTeamId(card.getTeam().getId());
        return compactCard;
    }

    private GoalDto goalToGoalDto(Goal goal) {
        GoalDto compactGoal = new GoalDto();
        compactGoal.setGoalType(goal.getGoalType());
        compactGoal.setMinute(goal.getMinute());
        compactGoal.setScorerId(goal.getScorer().getId());
        compactGoal.setTeamId(goal.getTeam().getId());
        return compactGoal;
    }
}
