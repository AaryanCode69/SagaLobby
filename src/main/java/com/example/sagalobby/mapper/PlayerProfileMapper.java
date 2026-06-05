package com.example.sagalobby.mapper;

import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import com.example.sagalobby.domain.postgres.playerprofile.dto.response.ProfileResponseDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PlayerProfileMapper {

    ProfileResponseDTO mapPlayerProfileToProfileResponse(PlayerProfile player);

}
