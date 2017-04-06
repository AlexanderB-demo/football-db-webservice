package de.balogha.footballint.dbrestservice.rest.controller;

import de.balogha.footballint.dbrestservice.model.Competition;
import de.balogha.footballint.dbrestservice.rest.hateoas.CompetitionResourceAssembler;
import de.balogha.footballint.dbrestservice.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/competitions")
public class CompetitionController {

    private CompetitionService competitionService;
    private CompetitionResourceAssembler competitionResourceAssembler;


    @Autowired
    public CompetitionController(CompetitionService competitionService,
                                 CompetitionResourceAssembler competitionResourceAssembler) {
        this.competitionService = competitionService;
        this.competitionResourceAssembler = competitionResourceAssembler;
    }


    @GetMapping
    public @ResponseBody PagedResources<Competition> findAllCompetitions(
            Pageable pageable,
            PagedResourcesAssembler pagedResourcesAssembler) {
        Page<Competition> competitions = competitionService.findAllCompetitions(pageable);
        return pagedResourcesAssembler.toResource(competitions, competitionResourceAssembler);
    }

    @GetMapping(value = "/{id}")
    public Resource<Competition> findCompetition(@PathVariable("id") long id) {
        Competition competition = competitionService.findCompetition(id);
        return competitionResourceAssembler.toResource(competition);
    }

    @GetMapping(value = "/{id}/games")
    public void findGamesOfCompetition(@PathVariable("id") Long id, HttpServletRequest req, HttpServletResponse res) {
        RequestDispatcher rd = req.getRequestDispatcher("/api/v1/games?competition=" + id);
        try {
            rd.forward(req, res);
        } catch (Exception e) {
            throw new RuntimeException("Could not forward request", e);
        }
    }

    @PostMapping
    public ResponseEntity<Resource<Competition>> addCompetition(@Valid @RequestBody Competition input) {
        input.setId(null);
        Competition result = competitionService.createCompetition(input);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(competitionResourceAssembler.toResource(result));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable("id") long id) {
        competitionService.deleteCompetition(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public Resource<Competition> updateCompetition(
            @PathVariable("id") long id,
            @Valid @RequestBody Competition input) {
        input.setId(id);
        Competition newState = competitionService.updateCompetition(input);
        return competitionResourceAssembler.toResource(newState);
    }

}
