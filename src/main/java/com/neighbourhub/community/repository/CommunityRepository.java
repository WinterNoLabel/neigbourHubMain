package com.neighbourhub.community.repository;

import com.neighbourhub.community.dto.CommunitySearchCriteria;
import com.neighbourhub.community.entity.Community;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Modifying
    @Query("update Community c set c.name = ?1, c.description = ?2 where c.id = ?3")
    int updateCommunity(String name, String description, Long id);


    @Transactional
    @Modifying
    @Query("update Community c set c.description = ?1 where c.id = ?2")
    int updateDescription(String description, Long id);
}
