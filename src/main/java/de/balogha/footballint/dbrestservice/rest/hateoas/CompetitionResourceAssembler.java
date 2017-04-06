package de.balogha.footballint.dbrestservice.rest.hateoas;

import de.balogha.footballint.dbrestservice.model.Competition;
import de.balogha.footballint.dbrestservice.rest.controller.CompetitionController;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class CompetitionResourceAssembler extends ResourceAssemblerSupport<Competition, Resource> {

    public CompetitionResourceAssembler() {
        super(CompetitionController.class, Resource.class);
    }

    @Override
    public List<Resource> toResources(Iterable<? extends Competition> entities) {
        List<Resource> resources = new ArrayList<>();
        entities.forEach(competition -> resources.add(toResource(competition)));
        return resources;
    }

    @Override
    public Resource<Competition> toResource(Competition entity) {
        TemplateVariable pageVar = new TemplateVariable("page", REQUEST_PARAM);
        TemplateVariable sizeVar = new TemplateVariable("size", REQUEST_PARAM);
        TemplateVariable sortVar = new TemplateVariable("sort", REQUEST_PARAM);
        TemplateVariable roundVar = new TemplateVariable("round", REQUEST_PARAM);
        TemplateVariable teamVar = new TemplateVariable("team", REQUEST_PARAM);
        TemplateVariable detailsVar = new TemplateVariable("details", REQUEST_PARAM);
        UriTemplate gameTemplate = new UriTemplate(
                linkTo(CompetitionController.class).slash(entity.getId()).slash("games").toString(),
                new TemplateVariables(pageVar, sizeVar, sortVar, roundVar, teamVar, detailsVar));

        return new Resource<>(
                entity,
                linkTo(CompetitionController.class).slash(entity.getId()).withSelfRel(),
                new Link(gameTemplate, "games"));
    }
}
