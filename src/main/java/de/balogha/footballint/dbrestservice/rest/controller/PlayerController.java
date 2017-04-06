package de.balogha.footballint.dbrestservice.rest.controller;

import de.balogha.footballint.dbrestservice.model.Player;
import de.balogha.footballint.dbrestservice.rest.hateoas.PlayerResourceAssembler;
import de.balogha.footballint.dbrestservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private PlayerService playerService;
    private PlayerResourceAssembler playerResourceAssembler;


    @Autowired
    public PlayerController(PlayerService playerService, PlayerResourceAssembler playerResourceAssembler) {
        this.playerService = playerService;
        this.playerResourceAssembler = playerResourceAssembler;
    }


    @GetMapping
    public PagedResources<Player> findAllPlayers(
            Pageable pageable,
            PagedResourcesAssembler pagedResourcesAssembler) {
        Page<Player> players = playerService.findAllPlayers(pageable);
        return pagedResourcesAssembler.toResource(players, playerResourceAssembler);
    }

    @GetMapping(value = "/{id}")
    public Resource<Player> findPlayer(@PathVariable("id") Long id) {
        Player player = playerService.findPlayer(id);
        return playerResourceAssembler.toResource(player);
    }

    @PostMapping
    public ResponseEntity<Resource<Player>> addPlayer(@Valid @RequestBody Player input) {
        input.setId(null);
        Player result = playerService.createPlayer(input);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(playerResourceAssembler.toResource(result));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable("id") Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    public Resource<Player> updatePlayer(
            @PathVariable("id") Long id,
            @Valid @RequestBody Player input) {
        input.setId(id);
        Player newState = playerService.updatePlayer(input);
        return playerResourceAssembler.toResource(newState);
    }

}
