package de.balogha.footballint.dbrestservice.repository;

import de.balogha.footballint.dbrestservice.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    //@Query("select g from Game g where g.competition.id = ?1")
    //List<Game> findByCompetition(Long competitionId);

    @Query("select g from Game g where g.homeTeam.id = ?1 or g.awayTeam.id = ?1")
    List<Game> findByTeam(Long teamId);

    //@Query("select g from Game g where g.round in ?1")
    //List<Game> findByCompetition(Collection<Integer> rounds);

    @Query("select g from Game g where g.competition.id = ?1 and (g.homeTeam.id = ?2 or g.awayTeam.id = ?2)")
    List<Game> findByCompetitionAndTeam(Long competitionId, Long teamId);




}
