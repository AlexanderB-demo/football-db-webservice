package de.balogha.footballint.dbrestservice.repository;


import de.balogha.footballint.dbrestservice.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
