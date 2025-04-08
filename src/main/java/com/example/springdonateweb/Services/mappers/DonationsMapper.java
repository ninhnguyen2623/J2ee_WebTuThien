package com.example.springdonateweb.Services.mappers;

import com.example.springdonateweb.Models.Dtos.Donations.DonationCreateDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationResponseDto;
import com.example.springdonateweb.Models.Dtos.Donations.DonationUpdateDto;
import com.example.springdonateweb.Models.Entities.DonationsEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface DonationsMapper {
    DonationsMapper INSTANCE = Mappers.getMapper(DonationsMapper.class);

    @Mapping(target = "donationDate", source = "donationDate", qualifiedByName = "stringToLocalDateTime")
    DonationsEntity toEntity(DonationCreateDto donationCreateDto);

    DonationsEntity toEntity(DonationUpdateDto donationUpdateDto);
    DonationResponseDto toDto(DonationsEntity donationsEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DonationsEntity partialUpdate(DonationUpdateDto donationUpdateDto, @MappingTarget DonationsEntity donationsEntity);

    @Named("stringToLocalDateTime")
    default LocalDateTime map(String donationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(donationDate, formatter);
    }
}
