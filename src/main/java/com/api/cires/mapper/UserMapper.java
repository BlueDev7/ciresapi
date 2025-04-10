package com.api.cires.mapper;

import com.api.cires.dto.UserProfileDTO;
import com.api.cires.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserProfileDTO userDTO);
    UserProfileDTO toUserDTO(User user);

}
