package com.neighbourhub.community.event;

import com.neighbourhub.community.entity.Community;
import com.neighbourhub.community.repository.CommunityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityEventService {

    private final CommunityEventRepository eventRepo;
    private final CommunityRepository communityRepo;

    @Transactional
    public CommunityEvent createEvent(Long communityId, Long creatorUserId, CommunityEvent event) {
        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        event.setCommunity(community);
        event.setCreatorUserId(creatorUserId);
        return eventRepo.save(event);
    }

    @Transactional
    public CommunityEvent updateEvent(Long eventId, CommunityEvent updatedEvent) {
        CommunityEvent event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        event.setName(updatedEvent.getName());
        event.setDescription(updatedEvent.getDescription());
        event.setEventDate(updatedEvent.getEventDate());
        event.setModifiedDate(LocalDateTime.now());

        return eventRepo.save(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        CommunityEvent event = eventRepo.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        eventRepo.softDelete(eventId);
    }

    @Transactional
    public List<CommunityEvent> getCommunityEvents(Long communityId) {
        return eventRepo.findAllByCommunityId(communityId);
    }
}