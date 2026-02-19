package org.usermanagement.usermanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.usermanagement.usermanagement.dto.UserDto;
import org.usermanagement.usermanagement.entity.base.UserEntity;
import org.usermanagement.usermanagement.exception.DuplicateEntityException;
import org.usermanagement.usermanagement.exception.ResourceNotFoundException;
import org.usermanagement.usermanagement.mapper.UserMapper;
import org.usermanagement.usermanagement.repository.UserRepository;
import org.usermanagement.usermanagement.service.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto testUserDto;
    private UserEntity testUserEntity;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        
        testUserDto = new UserDto();
        testUserDto.setId(testUserId);
        testUserDto.setFirstName("Dipesh");
        testUserDto.setLastName("Ghimire");
        testUserDto.setEmail("dipesh@example.com");
        testUserDto.setPhoneNumber("+1234567890");

        testUserEntity = UserEntity.builder()
                .id(testUserId)
                .firstName("Dipesh")
                .lastName("Ghimire")
                .email("dipesh@example.com")
                .phoneNumber("+1234567890")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should create user successfully when email does not exist")
    void createUser_Success() {
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(false);
        when(userMapper.toUserEntity(testUserDto)).thenReturn(testUserEntity);
        when(userRepository.save(testUserEntity)).thenReturn(testUserEntity);
        when(userMapper.toUserDto(testUserEntity)).thenReturn(testUserDto);

        UserDto result = userService.createUser(testUserDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(testUserDto.getEmail());
        assertThat(result.getFirstName()).isEqualTo(testUserDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(testUserDto.getLastName());
        
        verify(userRepository).existsByEmail(testUserDto.getEmail());
        verify(userRepository).save(any(UserEntity.class));
        verify(userMapper).toUserEntity(testUserDto);
        verify(userMapper).toUserDto(testUserEntity);
    }

    @Test
    @DisplayName("Should throw DuplicateEntityException when email already exists")
    void createUser_DuplicateEmail() {
        when(userRepository.existsByEmail(testUserDto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(testUserDto))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("User already exists with email");

        verify(userRepository).existsByEmail(testUserDto.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return user when valid ID is provided")
    void getUserById_Success() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUserEntity));
        when(userMapper.toUserDto(testUserEntity)).thenReturn(testUserDto);

        UserDto result = userService.getUserById(testUserId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getEmail()).isEqualTo(testUserDto.getEmail());
        
        verify(userRepository).findById(testUserId);
        verify(userMapper).toUserDto(testUserEntity);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user ID does not exist")
    void getUserById_NotFound() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(testUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");

        verify(userRepository).findById(testUserId);
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Should update user successfully when valid ID and data provided")
    void updateUser_Success() {
        UserDto updateDto = new UserDto();
        updateDto.setFirstName("Sagar");
        updateDto.setLastName("Malla");
        updateDto.setEmail("sagar@example.com");

        UserEntity updatedEntity = UserEntity.builder()
                .id(testUserId)
                .firstName("Sagar")
                .lastName("Malla")
                .email("sagar@example.com")
                .phoneNumber("+1234567890")
                .isActive(true)
                .build();

        UserDto updatedDto = new UserDto();
        updatedDto.setId(testUserId);
        updatedDto.setFirstName("Sagar");
        updatedDto.setLastName("Malla");
        updatedDto.setEmail("sagar@example.com");

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUserEntity));
        doNothing().when(userMapper).updateEntityFromDto(updateDto, testUserEntity);
        when(userRepository.save(testUserEntity)).thenReturn(updatedEntity);
        when(userMapper.toUserDto(updatedEntity)).thenReturn(updatedDto);

        UserDto result = userService.updateUser(updateDto, testUserId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUserId);
        assertThat(result.getFirstName()).isEqualTo("Sagar");
        assertThat(result.getLastName()).isEqualTo("Malla");
        
        verify(userRepository).findById(testUserId);
        verify(userMapper).updateEntityFromDto(updateDto, testUserEntity);
        verify(userRepository).save(testUserEntity);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent user")
    void updateUser_NotFound() {
        UserDto updateDto = new UserDto();
        updateDto.setFirstName("Sagar");
        
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(updateDto, testUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");

        verify(userRepository).findById(testUserId);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully when valid ID provided")
    void deleteUser_Success() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUserEntity));
        doNothing().when(userRepository).delete(testUserEntity);

        userService.deleteUser(testUserId);

        verify(userRepository).findById(testUserId);
        verify(userRepository).delete(testUserEntity);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void deleteUser_NotFound() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(testUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id");

        verify(userRepository).findById(testUserId);
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should return paginated user list successfully")
    void userList_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        
        UserEntity user2 = UserEntity.builder()
                .id(UUID.randomUUID())
                .firstName("Sagar")
                .lastName("Smith")
                .email("sagar@example.com")
                .phoneNumber("+9876543210")
                .isActive(true)
                .build();

        Page<UserEntity> userPage = new PageImpl<>(Arrays.asList(testUserEntity, user2), pageable, 2);
        
        UserDto userDto2 = new UserDto();
        userDto2.setFirstName("Sagar");
        userDto2.setLastName("Smith");
        userDto2.setEmail("sagar@example.com");

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toUserDto(testUserEntity)).thenReturn(testUserDto);
        when(userMapper.toUserDto(user2)).thenReturn(userDto2);

        Page<UserDto> result = userService.userList(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("dipesh@example.com");
        assertThat(result.getContent().get(1).getEmail()).isEqualTo("sagar@example.com");
        
        verify(userRepository).findAll(pageable);
    }
}
