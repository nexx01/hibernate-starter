package com.dmdev.mapper;

import com.dmdev.dao.CompanyRepository;
import com.dmdev.dto.UserCreateDTO;
import com.dmdev.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCreateDtoMapper implements Mapper<UserCreateDTO, User> {

    private final CompanyRepository companyRepository;
    @Override
    public User mapFrom(UserCreateDTO object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.username())
                .info(object.info())
                .role(object.role())
                .company(companyRepository.findById(object.companyId())
                        .orElseThrow(IllegalArgumentException::new))
                .build();
    }

}

