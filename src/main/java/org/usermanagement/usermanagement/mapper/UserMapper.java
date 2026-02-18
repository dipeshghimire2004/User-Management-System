package org.usermanagement.usermanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.usermanagement.usermanagement.dto.UserDto;
import org.usermanagement.usermanagement.entity.base.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(UserEntity user);

    UserEntity toUserEntity(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy= NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserDto userDto,@MappingTarget UserEntity user);
}
