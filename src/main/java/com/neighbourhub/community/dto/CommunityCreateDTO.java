package com.neighbourhub.community.dto;


import com.neighbourhub.community.entity.Community;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommunityCreateDTO {

    @NotBlank(message = "Название обязательно")
    @Size(min = 5, max = 100, message = "Название должно быть от 5 до 100 символов")
    private String name;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @NotNull(message = "ID создателя не может быть пустым")
    @Positive(message = "ID создателя должен быть положительным числом")
    private Long creatorId;

    public static Community toCommunity(CommunityCreateDTO dto) {
        Community community = new Community();
        community.setCreatorId(dto.creatorId);
        community.setDescription(dto.description);
        community.setName(dto.name);
        return community;
    }

    public static CommunityCreateDTO fromCommunity(Community community) {
        CommunityCreateDTO dto = new CommunityCreateDTO();
        dto.creatorId = community.getCreatorId();
        dto.description = community.getDescription();
        dto.name = community.getName();
        return dto;
    }

}


