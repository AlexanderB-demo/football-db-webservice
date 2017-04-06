package de.balogha.footballint.dbrestservice.repository;

import de.balogha.footballint.dbrestservice.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
