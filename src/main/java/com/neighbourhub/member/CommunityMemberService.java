package com.neighbourhub.member;

import com.neighbourhub.community.entity.Community;
import com.neighbourhub.community.repository.CommunityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityMemberService {

    private final CommunityMemberRepository communityMemberRepo;
    private final CommunityRepository communityRepo;

    @Transactional
    public void addMember(Long communityId, Long userId) {
        if (communityMemberRepo.existsById_CommunityIdAndId_UserId(communityId, userId)) {
            return;
        }

        Community community = communityRepo.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        CommunityMemberId id = new CommunityMemberId();
        id.setCommunityId(communityId);
        id.setUserId(userId);

        CommunityMember member = new CommunityMember();
        member.setId(id);
        member.setCommunity(community);
        communityMemberRepo.save(member);
    }

    @Transactional
    public void removeMember(Long communityId, Long userId) {
        if (!communityMemberRepo.existsById_CommunityIdAndId_UserId(communityId, userId)) {
            throw new EntityNotFoundException("User is not a member of the community");
        }

        communityMemberRepo.deleteById_CommunityIdAndId_UserId(communityId, userId);
    }

    @Transactional
    public List<Long> getMembers(Long communityId) {
        return communityMemberRepo.findAllByCommunityId(communityId)
                .stream()
                .map(CommunityMember::getUserId)
                .collect(Collectors.toList());
    }
}