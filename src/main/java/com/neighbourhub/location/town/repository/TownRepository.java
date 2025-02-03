package com.neighbourhub.location.town.repository;


import com.neighbourhub.location.town.entity.Town;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<Town, Long> {
}
