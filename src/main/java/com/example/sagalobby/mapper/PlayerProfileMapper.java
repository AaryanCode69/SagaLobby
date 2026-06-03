package com.example.sagalobby.mapper;

import com.example.sagalobby.domain.postgres.playerprofile.PlayerProfile;
import com.example.sagalobby.domain.postgres.playerprofile.dto.PlayerProfileResponseDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface PlayerProfileMapper {

    PlayerProfileResponseDTO mapUserToUserResponse(PlayerProfile player);

    PlayerProfile mapUserRequestToUser(PlayerProfileResponseDTO userRequestDTO);
}
