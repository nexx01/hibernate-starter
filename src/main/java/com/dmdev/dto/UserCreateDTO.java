package com.dmdev.dto;

import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.Role;

public record UserCreateDTO (PersonalInfo personalInfo,
                            String username,
                            String info,
                            Role role,
                            Integer companyId){
}
