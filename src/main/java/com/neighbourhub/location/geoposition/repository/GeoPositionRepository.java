package com.neighbourhub.location.geoposition.repository;

import com.neighbourhub.location.geoposition.entity.GeoPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoPositionRepository extends JpaRepository<GeoPosition, Long> {
}
