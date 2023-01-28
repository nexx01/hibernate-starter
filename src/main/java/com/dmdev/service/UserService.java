package com.dmdev.service;

import com.dmdev.dao.UserRepository;
import com.dmdev.dto.UserCreateDTO;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.User;
import com.dmdev.mapper.Mapper;
import com.dmdev.mapper.UserCreateDtoMapper;
import com.dmdev.mapper.UserReadDtoMapper;
import com.dmdev.validation.UpdateCheck;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadDtoMapper mapper;
    private final UserCreateDtoMapper userCreateDtoMapper;

    @Transactional
    public Long create(UserCreateDTO userCreateDTO) {
//validation
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();

        var validate = validator.validate(userCreateDTO, UpdateCheck.class);

        if(!validate.isEmpty()){
            throw new ConstraintViolationException(validate);
        }
        //map
        User userEntity = userCreateDtoMapper.mapFrom(userCreateDTO);
        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User,T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(), userRepository
                        .getEntityManager().getEntityGraph("withCompany")
        );

        return userRepository.findById(id,properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {

        Optional<User> maybeUser = userRepository.findById(1L);
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybeUser.isPresent();
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
     return findById(id,mapper);
    }

}
