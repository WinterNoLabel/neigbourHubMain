package com.neighbourhub.permission;

import com.neighbourhub.community.dto.CommunityCreateDTO;
import com.neighbourhub.community.dto.CommunityDTO;
import com.neighbourhub.community.service.CommunityService;
import com.neighbourhub.community.utils.CommunityMapper;
import com.neighbourhub.permissions.repository.PermissionRepository;
import com.neighbourhub.permissions.service.CommunityPermissionService;
import com.neighbourhub.permissions.CommunityPermissionType;
import com.neighbourhub.permissions.entity.Permission;
import com.neighbourhub.permissions.entity.Role;
import com.neighbourhub.permissions.entity.UserCommunityRole;
import com.neighbourhub.permissions.entity.UserCommunityRoleId;
import com.neighbourhub.permissions.repository.UserCommunityRoleRepository;
import com.neighbourhub.permissions.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class CommunityServiceTest {

    private static final Logger log = LogManager.getLogger(CommunityServiceTest.class);
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CommunityService communityService;
    @Autowired
    private CommunityPermissionService permissionService;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private UserCommunityRoleRepository roleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionRepository permissionRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @Test
    void createCommunity_ValidData_ShouldPersist() {
        CommunityCreateDTO dto = new CommunityCreateDTO("Dev Community", "For developers", 1L);

        CommunityDTO saved = communityService.createCommunity(dto);

        assertNotNull(saved.getId());
        assertEquals("Dev Community", saved.getName());
    }

    @Test
    void findById_ExistingId_ShouldReturnCommunity() {
        CommunityCreateDTO dto = new CommunityCreateDTO("Dev Community", "For developers", 1L);
        CommunityDTO saved = communityService.createCommunity(dto);

        CommunityDTO found = communityService.findById(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Dev Community", found.getName());
    }

    @Test
    void checkPermission_ValidUser_ShouldReturnTrue() {
        CommunityCreateDTO dto = new CommunityCreateDTO("Dev Community", "For developers", 1L);

        CommunityDTO community = communityService.createCommunity(dto);

        Permission editDescriptionPermission = permissionRepository.findByType(CommunityPermissionType.EDIT_DESCRIPTION).get();
        Role moderatorRole = roleService.createRole("Модератор", communityMapper.toEntity(community), Set.of(editDescriptionPermission));

        UserCommunityRole userRole = new UserCommunityRole();
        UserCommunityRoleId id = new UserCommunityRoleId();
        id.setUserId(1L);
        id.setCommunityId(community.getId());
        id.setRoleId(moderatorRole.getId());
        log.info(id.toString());
        userRole.setId(id);
        userRole.setCommunity(communityMapper.toEntity(community));
        userRole.setRole(moderatorRole);
        roleRepository.save(userRole);

        boolean hasPermission = permissionService.hasPermission(
                1L,
                community.getId(),
                CommunityPermissionType.EDIT_DESCRIPTION
        );

        assertTrue(hasPermission);
    }
}