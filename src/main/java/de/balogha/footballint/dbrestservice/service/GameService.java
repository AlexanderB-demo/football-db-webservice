package de.balogha.footballint.dbrestservice.service;

import de.balogha.footballint.dbrestservice.model.Game;
import de.balogha.footballint.dbrestservice.model.GameSearchQuery;
import de.balogha.footballint.dbrestservice.exception.NoSuchEntityException;
import de.balogha.footballint.dbrestservice.repository.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

import static java.lang.String.format;

@Service
public class GameService {

    private GameRepository gameRepository;
    private GameConverter gameConverter;

    @GetMapping
    public Page<Game> findAllGames(Pageable pageable, GameSearchQuery searchQuery) {
        return gameRepository.findAll(searchQuery, pageable);
    }

    public Game findGame(Long id) {
        Assert.notNull(id, "id must not be null");
        Game game = gameRepository.findOne(id);
        if (game == null) {
            throw new NoSuchEntityException(format("no game with id '%d' exists", id));
        }
        return game;
    }

    public Game createGame(Game newGame) {
        Assert.notNull(newGame, "newGame must not be null");
        return gameRepository.save(newGame);
    }

    public void deleteGame(Long id) {
        Assert.notNull(id, "id must not be null");
        if (!gameRepository.exists(id)) {
            throw new NoSuchEntityException(format("no game with id '%d' exists", id));
        }
        gameRepository.delete(id);
    }

    public Game updateGame(Game updatedGame) {
        Assert.notNull(updatedGame, "updatedGame must not be null");
        if (!gameRepository.exists(updatedGame.getId())) {
            throw new NoSuchEntityException(format("no game with id '%d' exists", updatedGame.getId()));
        }
        return gameRepository.save(updatedGame);
    }

}
