package de.balogha.footballint.dbrestservice.rest.controller;

import de.balogha.footballint.dbrestservice.model.Team;
import de.balogha.footballint.dbrestservice.rest.hateoas.TeamResourceAssembler;
import de.balogha.footballint.dbrestservice.service.TeamService;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private TeamService teamService;
    private TeamResourceAssembler teamResourceAssembler;


    @Autowired
    public TeamController(TeamService teamService,
                          TeamResourceAssembler teamResourceAssembler) {
        this.teamService = teamService;
        this.teamResourceAssembler = teamResourceAssembler;
    }


    @GetMapping
    public PagedResources<Team> findAllTeams(
            Pageable pageable,
            PagedResourcesAssembler pagedResourcesAssembler) {
        Page<Team> teams = teamService.findAllTeams(pageable);
        return pagedResourcesAssembler.toResource(teams, teamResourceAssembler);
    }

    @GetMapping(value = "/{id}")
    public Resource<Team> findTeam(@PathVariable("id") Long id) {
        Team team = teamService.findTeam(id);
        return teamResourceAssembler.toResource(team);
    }

    @GetMapping(value = "/{id}/games")
    public void findGamesOfTeam(@PathVariable("id") Long id, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/api/v1/games?team=" + id);
        rd.forward(req, res);
    }

    @PostMapping
    public ResponseEntity<Resource<Team>> addTeam(@Valid @RequestBody Team input) {
        input.setId(null);
        Team result = teamService.createTeam(input);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(teamResourceAssembler.toResource(result));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public Resource<Team> updateTeam(
            @PathVariable("id") Long id,
            @Valid @RequestBody Team input) {
        input.setId(id);
        Team newState = teamService.updateTeam(input);
        return teamResourceAssembler.toResource(newState);
    }
}