package de.balogha.footballint.dbrestservice.service;

import de.balogha.footballint.dbrestservice.model.Competition;
import de.balogha.footballint.dbrestservice.exception.NoSuchEntityException;
import de.balogha.footballint.dbrestservice.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static java.lang.String.format;

@Service
public class CompetitionService {

    private CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    public Page<Competition> findAllCompetitions(Pageable pageable) {
        return competitionRepository.findAll(pageable);
    }

    public Competition findCompetition(Long id) {
        Assert.notNull(id, "id must not be null");
        Competition competition = competitionRepository.findOne(id);
        if (competition == null) {
            throw new NoSuchEntityException(format("no competition with id '%d' exists", id));
        }
        return competition;
    }

    public Competition createCompetition(Competition newCompetition) {
        Assert.notNull(newCompetition, "newCompetition must not be null");
        return competitionRepository.save(newCompetition);
    }

    public void deleteCompetition(Long id) {
        Assert.notNull(id, "id must not be null");
        if (!competitionRepository.exists(id)) {
            throw new NoSuchEntityException(format("no competition with id '%d' exists", id));
        }
        competitionRepository.delete(id);
    }

    public Competition updateCompetition(Competition updatedCompetition) {
        Assert.notNull(updatedCompetition, "updatedCompetition must not be null");
        if (!competitionRepository.exists(updatedCompetition.getId())) {
            throw new NoSuchEntityException(format("no competition with id '%d' exists", updatedCompetition.getId()));
        }
        return competitionRepository.save(updatedCompetition);
    }

}
