package com.neighbourhub.location.community.service;

import com.neighbourhub.location.community.entity.CommunityLocation;
import com.neighbourhub.location.community.entity.CommunityLocationDto;
import com.neighbourhub.location.community.repository.CommunityLocationRepository;
import com.neighbourhub.location.district.repository.DistrictRepository;
import com.neighbourhub.location.geoposition.repository.GeoPositionRepository;
import com.neighbourhub.location.town.repository.TownRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import static com.neighbourhub.location.LocationType.*;

@RequiredArgsConstructor
@Service
public class CommunityLocationService {

    private final CommunityLocationRepository communityLocationRepository;
    private final TownRepository townRepo;
    private final DistrictRepository districtRepo;
    private final GeoPositionRepository geoRepo;

    public CommunityLocation create(CommunityLocation communityLocation) {
        validateLocation(communityLocation);

        return communityLocationRepository.save(communityLocation);
    }

    public CommunityLocation getCommunityLocation(Long communityId) throws EntityNotFoundException {
        return communityLocationRepository.findByCommunityId(communityId)
                .orElseThrow(EntityNotFoundException::new);
    }


    private void validateLocation(CommunityLocation dto) {
        switch (dto.getLocationType()) {
            case TOWN -> validateTownExists(dto.getLocationId());
            case DISTRICT -> validateDistrictExists(dto.getLocationId());
            case GEO -> validateGeoExists(dto.getLocationId());
            default -> throw new IllegalArgumentException("Invalid location type");
        }
    }

    private void validateTownExists(Long id) {
        townRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Town not found"));
    }

    private void validateDistrictExists(Long id) {
        districtRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("District not found"));
    }

    private void validateGeoExists(Long id) {
        geoRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Geo position not found"));
    }
}