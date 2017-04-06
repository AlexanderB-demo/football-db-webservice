package de.balogha.footballint.dbrestservice.rest.hateoas;

import de.balogha.footballint.dbrestservice.model.Player;
import de.balogha.footballint.dbrestservice.rest.controller.PlayerController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class PlayerResourceAssembler extends ResourceAssemblerSupport<Player, Resource> {

    public PlayerResourceAssembler() {
        super(PlayerController.class, Resource.class);
    }

    @Override
    public List<Resource> toResources(Iterable<? extends Player> entities) {
        List<Resource> resources = new ArrayList<>();
        entities.forEach(player -> resources.add(toResource(player)));
        return resources;
    }

    @Override
    public Resource<Player> toResource(Player entity) {
        return new Resource<>(entity, linkTo(PlayerController.class)
                .slash(entity.getId())
                .withSelfRel());
    }

}
