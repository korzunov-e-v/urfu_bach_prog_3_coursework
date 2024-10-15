package ru.ekorzunov.urfu_bach_prog_3_coursework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ekorzunov.urfu_bach_prog_3_coursework.dto.UserDto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Role;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.RoleRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.service.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class SecurityController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${app.admin.reg-token}")
    private String adminToken;


    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        log.info("Admin token = '" + adminToken + "'");
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", "null",
                    "На этот адрес электронной почты уже зарегестрирована учётная запись.");
        }

        if (result.hasErrors() || !userDto.isPasswordSet()) {
            model.addAttribute("user", userDto);
            return "redirect:/register";
        }

        if (userDto.getAdminToken().equals(adminToken)) {
            userService.saveUser(userDto, true);
        } else {
            userService.saveUser(userDto);
        }
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "list-users";
    }

    @PostMapping("/users/update/save")
    public String update(@Valid @ModelAttribute("user") UserDto userDto,
                         BindingResult result,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return String.format("redirect:/users/update?userId=%d", userDto.getId());
        }
        userService.updateUser(userDto);

        return "redirect:/users";
    }

    @GetMapping("/users/update")
    public String showRegistrationForm(@RequestParam Long userId, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request, Model model) {
        User existingUser = userService.findUserById(userId);
        UserDto userDto = userService.mapToUserDto(existingUser);
        model.addAttribute("user", userDto);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        model.addAttribute("userRoles", userDetail.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.toList()));
        return "update-user-form";
    }

}
