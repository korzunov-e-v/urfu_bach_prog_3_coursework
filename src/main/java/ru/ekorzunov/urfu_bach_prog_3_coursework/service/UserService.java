package ru.ekorzunov.urfu_bach_prog_3_coursework.service;

import org.springframework.stereotype.Service;
import ru.ekorzunov.urfu_bach_prog_3_coursework.dto.UserDto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;

import java.util.List;

@Service
public interface UserService {

    void saveUser(UserDto userDto);

    void saveUser(UserDto userDto, boolean isAdmin);

    User findUserByEmail(String email);

    User findUserById(long userId);

    List<UserDto> findAllUsers();

}
