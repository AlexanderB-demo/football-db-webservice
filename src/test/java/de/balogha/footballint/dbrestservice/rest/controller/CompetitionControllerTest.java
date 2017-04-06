package de.balogha.footballint.dbrestservice.rest.controller;

import de.balogha.footballint.dbrestservice.model.Competition;
import de.balogha.footballint.dbrestservice.repository.CompetitionRepository;
import de.balogha.footballint.dbrestservice.repository.GameRepository;
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

public class CompetitionControllerTest extends AbstractIntegrationTest {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private GameRepository gameRepository;

    private Competition existingCompetition1, existingCompetition2;

    @Before
    public void setup() throws Exception {
        gameRepository.deleteAllInBatch();
        competitionRepository.deleteAllInBatch();

        Competition c = new Competition();
        c.setYear(2006);
        c.setName("1. Bundesliga");
        c.setCountry("Germany");
        existingCompetition1 = competitionRepository.save(c);

        c = new Competition();
        c.setYear(2016);
        c.setName("Premiere League");
        c.setCountry("England");
        existingCompetition2 = competitionRepository.save(c);

    }

    @After
    public void cleanup() {
        competitionRepository.deleteAllInBatch();
    }

    @Test
    public void testSingleCompetitionIsCorrectlyFetchedById() throws Exception {
        mockMvc.perform(get("/api/v1/competitions/" + existingCompetition1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(existingCompetition1.getId()))
                .andExpect(jsonPath("$.name", is(existingCompetition1.getName())))
                .andExpect(jsonPath("$.country", is(existingCompetition1.getCountry())))
                .andExpect(jsonPath("$.year", is(existingCompetition1.getYear())))
                .andExpect(jsonPath("$._links.self.href", containsString("/competitions/" + existingCompetition1.getId())))
                .andExpect(jsonPath("$._links.games.href", containsString("/competitions/" + existingCompetition1.getId() + "/games")));
    }

    @Test
    public void testFetchingAllCompetitions() throws Exception {
        mockMvc.perform(get("/api/v1/competitions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.competitionList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.competitionList[?(@.id == '" + existingCompetition1.getId() + "')]").exists())
                .andExpect(jsonPath("$._embedded.competitionList[?(@.id == '" + existingCompetition2.getId() + "')]").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$.page.totalElements", is(2)));
    }

    @Test
    public void testCreatingCompetition() throws Exception {
        Competition newCompetition = new Competition();
        newCompetition.setYear(2000);
        newCompetition.setName("Serie A");
        newCompetition.setCountry("Italy");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(newCompetition)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(newCompetition.getName())))
                .andExpect(jsonPath("$.year", is(newCompetition.getYear())))
                .andExpect(jsonPath("$.country", is(newCompetition.getCountry())))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.games.href").exists());
        mockMvc.perform(get("/api/v1/competitions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.competitionList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.competitionList[?(@.name == '" + newCompetition.getName()
                        + "' && @.year == '" + newCompetition.getYear() + "')]").exists());
    }

    @Test
    public void testChangingCompetition() throws Exception {
        Competition changedCompetition = new Competition();
        changedCompetition.setId(existingCompetition1.getId());
        changedCompetition.setYear(2000);
        changedCompetition.setName("Serie A");
        changedCompetition.setCountry("Italy");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(changedCompetition)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(existingCompetition1.getId()))
                .andExpect(jsonPath("$.name", is(changedCompetition.getName())))
                .andExpect(jsonPath("$.year", is(changedCompetition.getYear())))
                .andExpect(jsonPath("$.country", is(changedCompetition.getCountry())))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.games.href").exists());
        mockMvc.perform(get("/api/v1/competitions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.competitionList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.competitionList[?(@.id == '" + existingCompetition1.getId()
                        + "' && @.name == '" + changedCompetition.getName()
                        + "' && @.year == '" + changedCompetition.getYear() + "')]").exists());
    }

    @Test
    public void testDeletingCompetition() throws Exception {
        mockMvc.perform(delete("/api/v1/competitions/" + existingCompetition1.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/competitions/" + existingCompetition1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCompetitionNotFoundWhenFetching() throws Exception {
        mockMvc.perform(get("/api/v1/competitions/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testChangingNonExistingCompetition() throws Exception {
        Competition c = new Competition();
        c.setYear(2000);
        c.setName("Serie A");
        c.setCountry("Italy");
        mockMvc.perform(put("/api/v1/competitions/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletingNonExistingCompetition() throws Exception {
        mockMvc.perform(delete("/api/v1/competitions/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMalformedBodyWhenCreatingCompetition() throws Exception {
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMalformedBodyWhenChangingCompetition() throws Exception {
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonEmptyNameConstraintWhenCreatingCompetition() throws Exception {
        Competition c;

        // name is empty
        c = new Competition();
        c.setName("");
        c.setYear(2000);
        c.setCountry("Italy");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());

        // name is null
        c = new Competition();
        c.setName(null);
        c.setYear(2000);
        c.setCountry("Italy");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonEmptyNameConstraintWhenChangingCompetition() throws Exception {
        Competition c;

        // name is empty
        c = new Competition();
        c.setName("");
        c.setYear(2000);
        c.setCountry("Italy");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());

        // name is null
        c = new Competition();
        c.setName(null);
        c.setYear(2000);
        c.setCountry("Italy");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonEmptyCountryConstraintWhenCreatingCompetition() throws Exception {
        Competition c;

        // name is empty
        c = new Competition();
        c.setName("Serie A");
        c.setYear(2000);
        c.setCountry("");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());

        // name is null
        c = new Competition();
        c.setName("Serie A");
        c.setYear(2000);
        c.setCountry(null);
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonEmptyCountryConstraintWhenChangingCompetition() throws Exception {
        Competition c;

        // name is empty
        c = new Competition();
        c.setName("Serie A");
        c.setYear(2000);
        c.setCountry("");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());

        // name is null
        c = new Competition();
        c.setName("Serie A");
        c.setYear(2000);
        c.setCountry(null);
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMaxNameLengthConstraintWhenCreatingCompetition() throws Exception {
        Competition c = new Competition();
        c.setId(existingCompetition1.getId());
        c.setName(String.join("", Collections.nCopies(256, "x")));  // 256 characters
        c.setYear(2000);
        c.setCountry("Italy");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMaxNameLengthConstraintWhenChangingCompetition() throws Exception {
        Competition c = new Competition();
        c.setId(existingCompetition1.getId());
        c.setName(String.join("", Collections.nCopies(256, "x")));  // 256 characters
        c.setYear(2000);
        c.setCountry("Italy");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMaxCountryLengthConstraintWhenCreatingCompetition() throws Exception {
        Competition c = new Competition();
        c.setId(existingCompetition1.getId());
        c.setName("Serie A");
        c.setYear(2000);
        c.setCountry(String.join("", Collections.nCopies(256, "x"))); // 256 characters
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMaxCountryLengthConstraintWhenChangingCompetition() throws Exception {
        Competition c = new Competition();
        c.setId(existingCompetition1.getId());
        c.setName("Serie A");
        c.setYear(2000);
        c.setCountry(String.join("", Collections.nCopies(256, "x"))); // 256 characters
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMinYearConstraintWhenCreatingCompetition() throws Exception {
        Competition c = new Competition();
        c.setId(existingCompetition1.getId());
        c.setName("Serie A");
        c.setYear(-1);
        c.setCountry("Italy");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMinYearConstraintWhenChangingCompetition() throws Exception {
        Competition c = new Competition();
        c.setId(existingCompetition1.getId());
        c.setName("Serie A");
        c.setYear(-1);
        c.setCountry("Italy");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUniqueNameYearConstraintWhenCreatingCompetition() throws Exception {
        Competition c;

        // same name and year ==> error
        c = new Competition();
        c.setName(existingCompetition1.getName());
        c.setYear(existingCompetition1.getYear());
        c.setCountry("xyz");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isConflict());

        // same year ==> OK
        c = new Competition();
        c.setName(existingCompetition1.getName());
        c.setYear(existingCompetition1.getYear() + 1);
        c.setCountry("xyz");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(c.getName())))
                .andExpect(jsonPath("$.year", is(c.getYear())))
                .andExpect(jsonPath("$.country", is(c.getCountry())));

        // same name ==> OK
        c = new Competition();
        c.setName(existingCompetition1.getName() + "x");
        c.setYear(existingCompetition1.getYear() + 1);
        c.setCountry("xyz");
        mockMvc.perform(post("/api/v1/competitions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(c.getName())))
                .andExpect(jsonPath("$.year", is(c.getYear())))
                .andExpect(jsonPath("$.country", is(c.getCountry())));
    }

    @Test
    public void testUniqueNameYearConstraintWhenChangingCompetition() throws Exception {
        Competition c;

        // same name and year ==> error
        c = new Competition();
        c.setName(existingCompetition1.getName());
        c.setYear(existingCompetition1.getYear());
        c.setCountry("xyz");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isConflict());

        // same year ==> OK
        c = new Competition();
        c.setName(existingCompetition1.getName());
        c.setYear(existingCompetition1.getYear() + 1);
        c.setCountry("xyz");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(c.getName())))
                .andExpect(jsonPath("$.year", is(c.getYear())))
                .andExpect(jsonPath("$.country", is(c.getCountry())));

        // same name ==> OK
        c = new Competition();
        c.setName(existingCompetition1.getName() + "x");
        c.setYear(existingCompetition1.getYear() + 1);
        c.setCountry("xyz");
        mockMvc.perform(put("/api/v1/competitions/" + existingCompetition2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(c)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(c.getName())))
                .andExpect(jsonPath("$.year", is(c.getYear())))
                .andExpect(jsonPath("$.country", is(c.getCountry())));
    }

}
