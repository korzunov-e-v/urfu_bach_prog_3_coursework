package ru.ekorzunov.urfu_bach_prog_3_coursework.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.UserAction;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserActionRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/useractions")
public class UserActionController {

    @Autowired
    private UserActionRepository userActionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getRentRecords() {
        return "redirect:./list";
    }

    @GetMapping("/list")
    public ModelAndView getAllRentRecords(@AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        List<UserAction> userActions;
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        if (is_admin) {
            userActions = userActionRepository.findAllByOrderByTimestampDesc();
        } else {
            long userId = userRepository.findByEmail(userDetail.getUsername()).getId();
            userActions = userActionRepository.findAllByUserIdOrderByTimestampDesc(userId);
        }

        ModelAndView mav = new ModelAndView("list-logs");
        mav.addObject("logs", userActions);
        return mav;
    }

}
