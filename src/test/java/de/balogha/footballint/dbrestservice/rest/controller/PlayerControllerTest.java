package de.balogha.footballint.dbrestservice.rest.controller;

import de.balogha.footballint.dbrestservice.model.Player;
import de.balogha.footballint.dbrestservice.repository.GameRepository;
import de.balogha.footballint.dbrestservice.repository.PlayerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PlayerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    private Player existingPlayer1, existingPlayer2;

    private SimpleDateFormat sdf;

    @Before
    public void setup() throws Exception {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        gameRepository.deleteAllInBatch();
        playerRepository.deleteAllInBatch();

        Player p = new Player();
        p.setForename("Karl");
        p.setSurname("Allgöwer");
        p.setBirthday(sdf.parse("1957-01-05"));
        existingPlayer1 = playerRepository.save(p);

        p = new Player();
        p.setForename("Robert");
        p.setSurname("Schlienz");
        p.setBirthday(sdf.parse("1924-02-03"));
        existingPlayer2 = playerRepository.save(p);

    }

    @After
    public void cleanup() {
        playerRepository.deleteAllInBatch();
    }

    @Test
    public void testSinglePlayerIsCorrectlyFetchedById() throws Exception {
        mockMvc.perform(get("/api/v1/players/" + existingPlayer1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(existingPlayer1.getId()))
                .andExpect(jsonPath("$.forename", is(existingPlayer1.getForename())))
                .andExpect(jsonPath("$.surname", is(existingPlayer1.getSurname())))
                .andExpect(jsonPath("$.birthday", is(sdf.format(existingPlayer1.getBirthday()))))
                .andExpect(jsonPath("$._links.self.href", containsString("/players/" + existingPlayer1.getId())));
    }

    @Test
    public void testFetchingAllPlayers() throws Exception {
        mockMvc.perform(get("/api/v1/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.playerList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.playerList[?(@.id == '" + existingPlayer1.getId() + "')]").exists())
                .andExpect(jsonPath("$._embedded.playerList[?(@.id == '" + existingPlayer2.getId() + "')]").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$.page.totalElements", is(2)));
    }

    @Test
    public void testCreatingPlayer() throws Exception {
        Player newPlayer = new Player();
        newPlayer.setForename("Karlheinz");
        newPlayer.setSurname("Förster");
        newPlayer.setBirthday(sdf.parse("1958-07-25"));
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(newPlayer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.forename", is(newPlayer.getForename())))
                .andExpect(jsonPath("$.surname", is(newPlayer.getSurname())))
                .andExpect(jsonPath("$.birthday", is(sdf.format(newPlayer.getBirthday()))))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/v1/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.playerList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.playerList[?(@.forename == '" + newPlayer.getForename()
                        + "' && @.surname == '" + newPlayer.getSurname() + "')]").exists());
    }

    @Test
    public void testChangingPlayer() throws Exception {
        Player changedPlayer = new Player();
        changedPlayer.setId(existingPlayer1.getId());
        changedPlayer.setForename("Karlheinz");
        changedPlayer.setSurname("Förster");
        changedPlayer.setBirthday(sdf.parse("1958-07-25"));
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(changedPlayer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(existingPlayer1.getId()))
                .andExpect(jsonPath("$.forename", is(changedPlayer.getForename())))
                .andExpect(jsonPath("$.surname", is(changedPlayer.getSurname())))
                .andExpect(jsonPath("$.birthday", is(sdf.format(changedPlayer.getBirthday()))))
                .andExpect(jsonPath("$._links.self.href").exists());
        mockMvc.perform(get("/api/v1/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.playerList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.playerList[?(@.id == '" + existingPlayer1.getId()
                        + "' && @.forename == '" + changedPlayer.getForename()
                        + "' && @.surname == '" + changedPlayer.getSurname() + "')]").exists());

    }

    @Test
    public void testDeletingPlayer() throws Exception {
        mockMvc.perform(delete("/api/v1/players/" + existingPlayer1.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/players/" + existingPlayer1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPlayerNotFoundWhenFetching() throws Exception {
        mockMvc.perform(get("/api/v1/players/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testChangingNonExistingPlayer() throws Exception {
        Player t = new Player();
        t.setForename("Karlheinz");
        t.setSurname("Förster");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(put("/api/v1/players/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletingNonExistingPlayer() throws Exception {
        mockMvc.perform(delete("/api/v1/players/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMalformedBodyWhenCreatingPlayer() throws Exception {
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMalformedBodyWhenChangingPlayer() throws Exception {
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonEmptyNameConstraintWhenCreatingPlayer() throws Exception {
        Player t;

        // forename is empty
        t = new Player();
        t.setForename("");
        t.setSurname("Förster");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // forename is null
        t = new Player();
        t.setForename(null);
        t.setSurname("Förster");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // surname is empty
        t = new Player();
        t.setForename("Karlheinz");
        t.setSurname("");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // surname is null
        t = new Player();
        t.setForename("Karlheinz");
        t.setSurname(null);
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonEmptyNameConstraintWhenChangingPlayer() throws Exception {
        Player t;

        // forename is empty
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename("");
        t.setSurname("Förster");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // forename is null
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename(null);
        t.setSurname("Förster");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // surname is empty
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename("Karlheinz");
        t.setSurname("");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // surname is null
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename("Karlheinz");
        t.setSurname(null);
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());



    }

    @Test
    public void testMaxNameLengthConstraintWhenCreatingPlayer() throws Exception {
        Player t;

        // forename has more than 255 character
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename(String.join("", Collections.nCopies(256, "x")));
        t.setSurname("Förster");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // surname has more than 255 character
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename("Karlheinz");
        t.setSurname(String.join("", Collections.nCopies(256, "x")));
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(post("/api/v1/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMaxNameLengthConstraintWhenChangingPlayer() throws Exception {
        Player t;

        // forename has more than 255 character
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename(String.join("", Collections.nCopies(256, "x")));
        t.setSurname("Förster");
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());

        // surname has more than 255 character
        t = new Player();
        t.setId(existingPlayer1.getId());
        t.setForename("Karlheinz");
        t.setSurname(String.join("", Collections.nCopies(256, "x")));
        t.setBirthday(new GregorianCalendar(1958, 6, 25).getTime());
        mockMvc.perform(put("/api/v1/players/" + existingPlayer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(t)))
                .andExpect(status().isBadRequest());
    }
}
