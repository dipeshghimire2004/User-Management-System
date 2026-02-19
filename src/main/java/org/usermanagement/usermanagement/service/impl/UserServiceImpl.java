package org.usermanagement.usermanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.usermanagement.usermanagement.dto.UserDto;
import org.usermanagement.usermanagement.entity.base.UserEntity;
import org.usermanagement.usermanagement.exception.DuplicateEntityException;
import org.usermanagement.usermanagement.exception.ResourceNotFoundException;
import org.usermanagement.usermanagement.mapper.UserMapper;
import org.usermanagement.usermanagement.repository.UserRepository;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        log.info("Create a new user");

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEntityException("User already exists with email: " + userDto.getEmail());
        }
        
        UserEntity user = userMapper.toUserEntity(userDto);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, UUID userId) {
        log.info("Update a user");
        UserEntity existingUser = findUserById(userId);
        userMapper.updateEntityFromDto(userDto, existingUser);
        UserEntity savedUser = userRepository.save(existingUser);
        return userMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public  UserDto getUserById(UUID userId){
        log.info("Get a user by id");
        UserEntity user = findUserById(userId);
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> userList(Pageable pageable){
        log.info("Get all users");
        Page<UserEntity> users = userRepository.findAll(pageable);
        return users.map(userMapper::toUserDto);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        log.info("Delete user by id: {}", userId);
        UserEntity user =findUserById(userId);
        userRepository.delete(user);
    }

    private UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

}


