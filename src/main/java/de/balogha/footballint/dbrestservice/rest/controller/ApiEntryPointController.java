package de.balogha.footballint.dbrestservice.rest.controller;

import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class ApiEntryPointController {

    @GetMapping(value = "/api/v1")
    public Resources apiEntryPoint() {
        List<Link> links = new ArrayList<>();

        TemplateVariable pageVar = new TemplateVariable("page", REQUEST_PARAM);
        TemplateVariable sizeVar = new TemplateVariable("size", REQUEST_PARAM);
        TemplateVariable sortVar = new TemplateVariable("sort", REQUEST_PARAM);
        TemplateVariable roundVar = new TemplateVariable("round", REQUEST_PARAM);
        TemplateVariable competitionVar = new TemplateVariable("competition", REQUEST_PARAM);
        TemplateVariable teamVar = new TemplateVariable("team", REQUEST_PARAM);
        TemplateVariable detailsVar = new TemplateVariable("details", REQUEST_PARAM);

        TemplateVariables pageableVars = new TemplateVariables(pageVar, sizeVar, sortVar);
        links.add(new Link(new UriTemplate(linkTo(TeamController.class).toString(), pageableVars), "teams"));
        links.add(new Link(new UriTemplate(linkTo(PlayerController.class).toString(), pageableVars), "players"));
        links.add(new Link(new UriTemplate(linkTo(CompetitionController.class).toString(), pageableVars), "competitions"));

        TemplateVariables gamesVars = new TemplateVariables(pageVar, sizeVar, sortVar, roundVar, competitionVar, teamVar, detailsVar);
        UriTemplate gameTemplate = new UriTemplate(linkTo(GameController.class).toString(), gamesVars);
        links.add(new Link(gameTemplate, "games"));
        return new Resources<>(Collections.emptySet(), links);
    }
}
