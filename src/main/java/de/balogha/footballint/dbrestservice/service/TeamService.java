package de.balogha.footballint.dbrestservice.service;

import de.balogha.footballint.dbrestservice.exception.NoSuchEntityException;
import de.balogha.footballint.dbrestservice.model.Team;
import de.balogha.footballint.dbrestservice.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static java.lang.String.format;

@Service
public class TeamService {

    private TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Page<Team> findAllTeams(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }

    public Team findTeam(Long id) {
        Assert.notNull(id, "id must not be null");
        Team team = teamRepository.findOne(id);
        if (team == null) {
            throw new NoSuchEntityException(format("no team with id '%d' exists", id));
        }
        return team;
    }

    public Team createTeam(Team newTeam) {
        Assert.notNull(newTeam, "newTeam must not be null");
        return teamRepository.save(newTeam);
    }

    public void deleteTeam(Long id) {
        Assert.notNull(id, "id must not be null");
        if (!teamRepository.exists(id)) {
            throw new NoSuchEntityException(format("no team with id '%d' exists", id));
        }
        teamRepository.delete(id);
    }

    public Team updateTeam(Team updatedTeam) {
        Assert.notNull(updatedTeam, "updatedTeam must not be null");
        if (!teamRepository.exists(updatedTeam.getId())) {
            throw new NoSuchEntityException(format("no team with id '%d' exists", updatedTeam.getId()));
        }
        return teamRepository.save(updatedTeam);
    }

}
