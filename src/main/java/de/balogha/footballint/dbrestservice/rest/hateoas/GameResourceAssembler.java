package de.balogha.footballint.dbrestservice.rest.hateoas;

import de.balogha.footballint.dbrestservice.model.Game;
import de.balogha.footballint.dbrestservice.rest.controller.GameController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class GameResourceAssembler extends ResourceAssemblerSupport<Game, Resource> {

    public GameResourceAssembler() {
        super(GameController.class, Resource.class);
    }
    
    @Override
    public List<Resource> toResources(Iterable<? extends Game> entities) {
        List<Resource> resources = new ArrayList<>();
        entities.forEach(game -> resources.add(toResource(game)));
        return resources;
    }

    @Override
    public Resource<Game> toResource(Game entity) {
        return new Resource<>(entity, linkTo(GameController.class)
                .slash(entity.getId())
                .withSelfRel());
    }
}
