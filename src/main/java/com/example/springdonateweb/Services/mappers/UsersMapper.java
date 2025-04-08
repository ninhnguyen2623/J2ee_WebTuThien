package com.example.springdonateweb.Services.mappers;
import com.example.springdonateweb.Models.Dtos.Users.UserAddDto;
import com.example.springdonateweb.Models.Dtos.Users.UserCreateDto;
import com.example.springdonateweb.Models.Dtos.Users.UserUpdateDto;
import com.example.springdonateweb.Models.Dtos.Users.UsersResponseDto;
import com.example.springdonateweb.Models.Entities.UsersEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",uses = {UsersMapper.class})
public interface UsersMapper {
//    @Mapping(target = "userId", source = "userId")
    UsersEntity toDto(UsersEntity usersEntity);
    UsersEntity toEntity(UsersEntity usersEntity);
    UsersEntity toEntity(UserCreateDto userCreateDto);
    UsersEntity toEntity(UserUpdateDto userUpdateDto);

    UsersEntity toEntity(UsersResponseDto usersResponseDto);

    //    UsersEntity toEntity(UsersResponseDto usersResponseDto);
    UsersEntity toEntity(UserAddDto userAddDto);


    UsersResponseDto toResponseDto(UsersEntity usersEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UsersEntity partialUpdate(UserUpdateDto userUpdateDto, @MappingTarget UsersEntity usersEntity);




//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    UsersEntity partialUpdate(UserUpdateDto userUpdateDto, @MappingTarget UsersEntity usersEntity);
}
