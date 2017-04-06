package de.balogha.footballint.dbrestservice.rest.hateoas;


import de.balogha.footballint.dbrestservice.model.Team;
import de.balogha.footballint.dbrestservice.rest.controller.TeamController;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class TeamResourceAssembler extends ResourceAssemblerSupport<Team, Resource> {

    public TeamResourceAssembler() {
        super(TeamController.class, Resource.class);
    }

    @Override
    public List<Resource> toResources(Iterable<? extends Team> entities) {
        List<Resource> resources = new ArrayList<>();
        entities.forEach(team -> resources.add(toResource(team)));
        return resources;
    }

    @Override
    public Resource<Team> toResource(Team entity) {
        TemplateVariable pageVar = new TemplateVariable("page", REQUEST_PARAM);
        TemplateVariable sizeVar = new TemplateVariable("size", REQUEST_PARAM);
        TemplateVariable sortVar = new TemplateVariable("sort", REQUEST_PARAM);
        TemplateVariable roundVar = new TemplateVariable("round", REQUEST_PARAM);
        TemplateVariable competitionVar = new TemplateVariable("competition", REQUEST_PARAM);
        TemplateVariable detailsVar = new TemplateVariable("details", REQUEST_PARAM);
        UriTemplate gameTemplate = new UriTemplate(
                linkTo(TeamController.class).slash(entity.getId()).slash("games").toString(),
                new TemplateVariables(pageVar, sizeVar, sortVar, roundVar, competitionVar, detailsVar));

        return new Resource<>(
                entity,
                linkTo(TeamController.class).slash(entity.getId()).withSelfRel(),
                new Link(gameTemplate, "games"));
    }
}
