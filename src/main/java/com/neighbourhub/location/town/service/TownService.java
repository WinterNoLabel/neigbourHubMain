package com.neighbourhub.location.town.service;

import com.neighbourhub.location.town.repository.TownRepository;
import org.springframework.stereotype.Service;

@Service
public class TownService {

    private final TownRepository townRepository;

    public TownService(TownRepository townRepository) {
        this.townRepository = townRepository;
    }

}
