package de.balogha.footballint.dbrestservice.service;

import de.balogha.footballint.dbrestservice.exception.NoSuchEntityException;
import de.balogha.footballint.dbrestservice.model.Player;
import de.balogha.footballint.dbrestservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static java.lang.String.format;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Page<Player> findAllPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    public Player findPlayer(Long id) {
        Assert.notNull(id, "id must not be null");
        Player player = playerRepository.findOne(id);
        if (player == null) {
            throw new NoSuchEntityException(format("no player with id '%d' exists", id));
        }
        return player;
    }

    public Player createPlayer(Player newPlayer) {
        Assert.notNull(newPlayer, "newTeam must not be null");
        return playerRepository.save(newPlayer);
    }

    public void deletePlayer(Long id) {
        Assert.notNull(id, "id must not be null");
        if (!playerRepository.exists(id)) {
            throw new NoSuchEntityException(format("no player with id '%d' exists", id));
        }
        playerRepository.delete(id);
    }

    public Player updatePlayer(Player updatedPlayer) {
        Assert.notNull(updatedPlayer, "updatedPlayer must not be null");
        if (!playerRepository.exists(updatedPlayer.getId())) {
            throw new NoSuchEntityException(format("no player with id '%d' exists", updatedPlayer.getId()));
        }
        return playerRepository.save(updatedPlayer);
    }

}
