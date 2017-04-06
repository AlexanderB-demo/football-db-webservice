package de.balogha.footballint.dbrestservice.model;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class GameSearchQuery implements Specification<Game> {

    private final Specification<Game> delegateSpec;

    public GameSearchQuery(Specification<Game> delegateSpec) {
        this.delegateSpec = delegateSpec;
    }

    @Override
    public Predicate toPredicate(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return delegateSpec.toPredicate(root, query, cb);
    }


    public static class Builder {

        private Long teamId;
        private Long competitionId;
        private List<Integer> rounds = new ArrayList<>();

        public Builder rounds(Collection<Integer> rounds) {
            this.rounds.addAll(rounds);
            return this;
        }

        public Builder team(long teamId) {
            this.teamId = teamId;
            return this;
        }

        public Builder competition(long competitionId) {
            this.competitionId = competitionId;
            return this;
        }

        public GameSearchQuery build() {
            Specification<Game> delegateSpec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (competitionId != null) {
                    predicates.add(
                            cb.equal(root.<Competition>get("competition").<Long>get("id"), competitionId));
                }

                if (teamId != null) {
                    predicates.add(
                            cb.or(
                                    cb.equal(root.<Team>get("homeTeam").<Long>get("id"), id),
                                    cb.equal(root.<Team>get("awayTeam").<Long>get("id"), id)
                            )
                    );

                }

                if (!rounds.isEmpty()) {
                    predicates.add(root.get("round").in(rounds));
                }
                return cb.and(predicates.toArray(new Predicate[] {}));
            };

            return new GameSearchQuery(delegateSpec);
        }

    }
}
