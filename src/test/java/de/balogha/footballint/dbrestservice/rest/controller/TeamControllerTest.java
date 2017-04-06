package de.balogha.footballint.dbrestservice.rest.controller;

import de.balogha.footballint.dbrestservice.model.Team;
import de.balogha.footballint.dbrestservice.repository.GameRepository;
import de.balogha.footballint.dbrestservice.repository.TeamRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TeamControllerTest extends AbstractIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    private Team existingTeam1, existingTeam2;

    @Before
    public void setup() throws Exception {
        gameRepository.delete(gameRepository.findAll());
        teamRepository.deleteAllInBatch();

        Team t = new Team();
        t.setName("SSV Reutlingen");
        existingTeam1 = teamRepository.save(t);

        t = new Team();
        t.setName("VfB Stuttgart");
        existingTeam2 = teamRepository.save(t);
    }

    @After
    public void cleanup() {
        teamRepository.deleteAllInBatch();
    }

    @Test
    public void testSingleTeamIsCorrectlyFetchedById() throws Exception {
        mockMvc.perform(get("/api/v1/teams/" + existingTeam1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(existingTeam1.getId()))
                .andExpect(jsonPath("$.name", is(existingTeam1.getName())))
                .andExpect(jsonPath("$._links.self.href", containsString("/teams/" + existingTeam1.getId())))
                .andExpect(jsonPath("$._links.games.href", containsString("/teams/" + existingTeam1.getId() + "/games")));
    }

    @Test
    public void testFetchingAllTeams() throws Exception {
        mockMvc.perform(get("/api/v1/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.teamList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.teamList[?(@.id == '" + existingTeam1.getId() + "')]").exists())
                .andExpect(jsonPath("$._embedded.teamList[?(@.id == '" + existingTeam2.getId() + "')]").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$.page.totalElements", is(2)));
    }

    @Test
    public void testCreatingTeam() throws Exception {
        Team newTeam = new Team();
        newTeam.setName("1. FC Kaiserslautern");
        mockMvc.perform(post("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(newTeam)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(newTeam.getName())))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.games.href").exists());
        mockMvc.perform(get("/api/v1/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.teamList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.teamList[?(@.name == '" + newTeam.getName() + "')]").exists());
    }

    @Test
    public void testChangingTeam() throws Exception {
        Team changedTeam = new Team();
        changedTeam.setId(existingTeam1.getId());
        changedTeam.setName("AC Cesena");
        mockMvc.perform(put("/api/v1/teams/" + existingTeam1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(changedTeam)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(existingTeam1.getId()))
                .andExpect(jsonPath("$.name", is(changedTeam.getName())));
        mockMvc.perform(get("/api/v1/teams"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.teamList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.teamList[?(@.id == '" + existingTeam1.getId()
                        + "' && @.name == '" + changedTeam.getName() + "')]").exists());

    }

    @Test
    public void testDeletingTeam() throws Exception {
        mockMvc.perform(delete("/api/v1/teams/" + existingTeam1.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/teams/" + existingTeam1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testTeamNotFoundWhenFetching() throws Exception {
        mockMvc.perform(get("/api/v1/teams/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testChangingNonExistingTeam() throws Exception {
        Team t = new Team();
        t.setName("xxx");
        mockMvc.perform(put("/api/v1/teams/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletingNonExistingTeam() throws Exception {
        mockMvc.perform(delete("/api/v1/teams/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMalformedBodyWhenCreatingTeam() throws Exception {
        mockMvc.perform(post("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMalformedBodyWhenChangingTeam() throws Exception {
        mockMvc.perform(put("/api/v1/teams/" + existingTeam1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUniqueNameConstraintWhenCreatingTeam() throws Exception {
        mockMvc.perform(post("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(existingTeam1)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUniqueNameConstraintWhenChangingTeam() throws Exception {
        Team t = new Team();
        t.setId(existingTeam1.getId());
        t.setName(existingTeam2.getName());
        mockMvc.perform(put("/api/v1/teams/" + existingTeam1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testNonEmptyNameConstraintWhenCreatingTeam() throws Exception {
        Team t;
        // name is empty
        t = new Team();
        t.setName("");
        mockMvc.perform(post("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // name is null
        t = new Team();
        t.setName(null);
        mockMvc.perform(post("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonEmptyNameConstraintWhenChangingTeam() throws Exception {
        Team t1 = new Team();
        t1.setName("");
        t1.setId(existingTeam1.getId());
        Team t2 = new Team();
        t2.setName(null);
        t2.setId(existingTeam1.getId());

        mockMvc.perform(put("/api/v1/teams/" + existingTeam1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t1)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/v1/teams/" + existingTeam1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMaxNameLengthConstraintWhenCreatingTeam() throws Exception {
        Team t1 = new Team();
        t1.setName( String.join("", Collections.nCopies(256, "x")) );  // create name of size 256
        mockMvc.perform(post("/api/v1/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMaxNameLengthConstraintWhenChangingTeam() throws Exception {
        Team t1 = new Team();
        String.join("", Collections.nCopies(256, "x"));
        t1.setName( String.join("", Collections.nCopies(256, "x")) );  // create name of size 256
        t1.setId(existingTeam1.getId());
        mockMvc.perform(put("/api/v1/teams/" + existingTeam1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t1)))
                .andExpect(status().isBadRequest());
    }
}
