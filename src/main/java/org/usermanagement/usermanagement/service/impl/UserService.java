package org.usermanagement.usermanagement.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.usermanagement.usermanagement.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, UUID userId);

    UserDto getUserById(UUID userId);

    Page<UserDto> userList(Pageable pageable);

    void deleteUser(UUID userId);
}
