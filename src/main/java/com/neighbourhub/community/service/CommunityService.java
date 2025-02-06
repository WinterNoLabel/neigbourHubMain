package com.neighbourhub.community.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.neighbourhub.community.dto.CommunityCreateDTO;
import com.neighbourhub.community.dto.CommunityDTO;
import com.neighbourhub.community.dto.CommunitySearchCriteria;
import com.neighbourhub.community.entity.Community;
import com.neighbourhub.community.firebase.CommunityFirebaseChat;
import com.neighbourhub.community.firebase.CommunityFirebaseChatRepository;
import com.neighbourhub.community.firebase.FirebaseService;
import com.neighbourhub.community.firebase.dto.CreateCommunityFirebaseDto;
import com.neighbourhub.community.repository.CommunityRepository;
import com.neighbourhub.community.utils.CommunityMapper;
import com.neighbourhub.member.CommunityMemberService;
import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final RoleService roleService;
    private final CommunityMemberService communityMemberService;
    private final FirebaseService firebaseService;

    private final CommunityFirebaseChatRepository communityFirebaseChatRepository;

    public CommunityDTO createCommunity(CommunityCreateDTO dto) {
        Community community = CommunityCreateDTO.toCommunity(dto);
        community.setCreatedAt(LocalDateTime.now());

        Community createdCommunity = communityRepository.save(community);
        communityMemberService.addMember(createdCommunity.getId(), dto.getCreatorId());

        Role creatorRole = roleService.createRole("Создатель", createdCommunity.getId(), CommunityPermissionType.allPerms());
        roleService.assignRoleToUser(dto.getCreatorId(), creatorRole.getId(), createdCommunity.getId());

        initFirebaseCommunityChat(createdCommunity);

        return communityMapper.toDto(createdCommunity);
    }

    private void initFirebaseCommunityChat(Community createdCommunity) {
        firebaseService.getFirestore().ifPresent(
                it -> {
                    CollectionReference rooms = it.collection("rooms");
                    CreateCommunityFirebaseDto firebaseDto = new CreateCommunityFirebaseDto();
                    firebaseDto.setName(createdCommunity.getName());
                    firebaseDto.setCommunityId(createdCommunity.getId());
                    firebaseDto.setCreatorId(createdCommunity.getCreatorId());
                    firebaseDto.setCreatedAt(Instant.now());
                    ApiFuture<DocumentReference> added = rooms.add(firebaseDto);
                    try {
                        CommunityFirebaseChat communityFirebaseChat = new CommunityFirebaseChat();
                        communityFirebaseChat.setCommunity(createdCommunity);
                        communityFirebaseChat.setChatId(added.get().getId());

                        communityFirebaseChatRepository.save(communityFirebaseChat);
                    } catch (InterruptedException | ExecutionException ignored) {

                    }
                }
        );
    }

    public List<CommunityDTO> searchCommunities(CommunitySearchCriteria criteria) {
        List<Community> communities = communityRepository.searchCommunities(criteria);
        return communityMapper.toDtoList(communities);
    }

    public Integer updateCommunity(CommunityDTO dto) {
        return communityRepository.updateCommunity(dto.getName(), dto.getDescription(), dto.getId());
    }

    public Integer updateDescription(String description, Long id) {
        return communityRepository.updateDescription(description, id);
    }

    public CommunityDTO findById(Long id) {
        return communityMapper.toDto(communityRepository.findById(id).orElse(null));
    }

    @Transactional
    public CommunityDTO updatePhoto(Long communityId, String photoUrl) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Сообщество не найдено"));

        community.setPhotoUrl(photoUrl);
        return communityMapper.toDto(community);
    }
}