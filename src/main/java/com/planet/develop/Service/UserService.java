package com.planet.develop.Service;

import com.planet.develop.DTO.UserDTO;
import com.planet.develop.Entity.User;

public interface UserService {

    /** 사용자 등록 */
    String register(UserDTO dto);

    default User dtoToEntity(UserDTO dto) {

        User entity = User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .build();

        return entity;
    }

}
