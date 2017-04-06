package de.balogha.footballint.dbrestservice.repository;

import de.balogha.footballint.dbrestservice.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
}
