package de.balogha.footballint.dbrestservice.rest.controller;

import de.balogha.footballint.dbrestservice.model.*;
import de.balogha.footballint.dbrestservice.rest.dto.GameDto;
import de.balogha.footballint.dbrestservice.rest.hateoas.GameResourceAssembler;
import de.balogha.footballint.dbrestservice.service.GameConverter;
import de.balogha.footballint.dbrestservice.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.*;
import java.net.URI;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private GameResourceAssembler gameResourceAssembler;
    private GameConverter gameConverter;
    private GameService gameService;


    @Autowired
    public GameController(GameResourceAssembler gameResourceAssembler, GameConverter gameConverter, GameService gameService) {
        this.gameResourceAssembler = gameResourceAssembler;
        this.gameConverter = gameConverter;
        this.gameService = gameService;
    }


    @GetMapping
    public PagedResources<GameDto> findAllGames(
            Pageable pageable,
            PagedResourcesAssembler assembler,
            @RequestParam(value = "competition") Optional<Long> competitionId,
            @RequestParam(value = "team") Optional<Long> teamId,
            @RequestParam(value = "round", required = false) List<Integer> round) {
        GameSearchQuery searchQuery = createGameQuery(competitionId, teamId, round);
        Page<Game> games = gameService.findAllGames(pageable, searchQuery);
        return assembler.toResource(convertGamePageToGameDtoPage(games));
    }

    @GetMapping(value = "/{id}")
    public Resource<Game> findGame(@PathVariable("id") Long id) {
        Game game = gameService.findGame(id);
        return gameResourceAssembler.toResource(game);
    }

    @PostMapping
    public ResponseEntity<Resource<Game>> addGame(@Valid @RequestBody GameDto input) {
        Game newGame = gameConverter.gameDtoToGame(input);
        validateGame(newGame);
        Game result = gameService.createGame(newGame);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(gameResourceAssembler.toResource(result));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable("id") Long id) {
        gameService.deleteGame(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/{id}")
    public Resource<Game> updateGame(
            @PathVariable("id") Long id,
            @Valid @RequestBody GameDto input) {
        input.setId(id);
        Game detailedInput = gameConverter.gameDtoToGame(input);
        validateGame(detailedInput);
        Game newState = gameService.updateGame(detailedInput);
        return gameResourceAssembler.toResource(newState);
    }

    private GameSearchQuery createGameQuery(final Optional<Long> competitionId, final Optional<Long> teamId, final Collection<Integer> rounds) {
        GameSearchQuery.Builder queryBuilder = new GameSearchQuery.Builder();
        competitionId.ifPresent(queryBuilder::competition);
        teamId.ifPresent(queryBuilder::team);
        queryBuilder.rounds(rounds);
        return queryBuilder.build();
    }

    private Page<GameDto> convertGamePageToGameDtoPage(Page<Game> gamePage) {
        return gamePage.map(gameConverter::gameToGameDto);
    }

    private void validateGame(Game game) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Game>> errors = validator.validate(game);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }

}
