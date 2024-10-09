package ru.ekorzunov.urfu_bach_prog_3_coursework.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.ekorzunov.urfu_bach_prog_3_coursework.dto.UserDto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.service.UserService;

import java.util.List;

@Controller
@Slf4j
public class SecurityController {

    private final UserService userService;

    @Value("${app.admin.reg-token}")
    private String adminToken;


    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
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

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
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
        return "users";
    }

}
