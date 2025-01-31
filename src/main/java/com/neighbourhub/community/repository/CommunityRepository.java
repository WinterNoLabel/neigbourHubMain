package com.neighbourhub.community.repository;

import com.neighbourhub.community.dto.CommunitySearchCriteria;
import com.neighbourhub.community.entity.Community;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>, JpaSpecificationExecutor<Community> {
    default List<Community> searchCommunities(CommunitySearchCriteria criteria) {
        return findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getId() != null) {
                predicates.add(cb.equal(root.get("id"), criteria.getId()));
            }
            if (criteria.getName() != null) {
                predicates.add(cb.like(root.get("name"), "%" + criteria.getName() + "%"));
            }
            if (criteria.getDescription() != null) {
                predicates.add(cb.like(root.get("description"), "%" + criteria.getDescription() + "%"));
            }
            if (criteria.getCreatorId() != null) {
                predicates.add(cb.equal(root.get("creatorId"), criteria.getCreatorId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

}
