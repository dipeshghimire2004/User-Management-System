package org.usermanagement.usermanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.usermanagement.usermanagement.constant.ApiConstants;
import org.usermanagement.usermanagement.dto.GlobalApiRequest;
import org.usermanagement.usermanagement.dto.GlobalApiResponse;
import org.usermanagement.usermanagement.dto.UserDto;
import org.usermanagement.usermanagement.service.impl.UserService;

import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.User.USER_BASE_API)
@Slf4j
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;

    @PostMapping(ApiConstants.User.CREATE_USER_API)
    public ResponseEntity<GlobalApiResponse<UserDto>> createUser(@RequestBody @Valid GlobalApiRequest<UserDto> request) {
        UserDto createdUser = userService.createUser(request.getData());
        return ResponseEntity.status(HttpStatus.CREATED).body(GlobalApiResponse.success(createdUser));
    }

    @PatchMapping(ApiConstants.User.UPDATE_USER_API)
    public ResponseEntity<GlobalApiResponse<UserDto>> updateUser(@RequestBody @Valid GlobalApiRequest<UserDto> request, @PathVariable UUID userId) {
        UserDto updatedUser = userService.updateUser(request.getData(), userId);
        return ResponseEntity.status(HttpStatus.OK).body(GlobalApiResponse.success(updatedUser));
    }

    @GetMapping(ApiConstants.User.GET_USER)
    public ResponseEntity<GlobalApiResponse<UserDto>> getUser(@PathVariable UUID userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(GlobalApiResponse.success(user));
    }

    @GetMapping(ApiConstants.User.GET_USERS)
    public ResponseEntity<GlobalApiResponse<Page<UserDto>>> getUsers(Pageable pageable) {
        Page<UserDto> users = userService.userList(pageable);
        return ResponseEntity.ok(GlobalApiResponse.success(users));
    }

    @DeleteMapping(ApiConstants.User.DELETE_USER_API)
    public ResponseEntity<GlobalApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(GlobalApiResponse.success(null));
    }
}
