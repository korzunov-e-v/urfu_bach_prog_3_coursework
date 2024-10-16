package ru.ekorzunov.urfu_bach_prog_3_coursework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ekorzunov.urfu_bach_prog_3_coursework.dto.UserDto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Role;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.RoleRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        boolean isAdmin = false;
        saveUser(userDto, isAdmin);
    }

    @Override
    public void saveUser(UserDto userDto, boolean isAdmin) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role;
        if (isAdmin) {
            role = checkRoleExist("ADMIN");
        } else {
            role = checkRoleExist("READ_ONLY");
        }

        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    public void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        if (userDto.isPasswordSet()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setRoles(userDto.getRoles());
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    private Role checkRoleExist(String roleName) {
        Role role = roleRepository.findByName("ROLE_" + roleName);
        if (role == null) {
            role = new Role();
            role.setName("ROLE_" + roleName);
            roleRepository.save(role);
        }

        return role;
    }

}
