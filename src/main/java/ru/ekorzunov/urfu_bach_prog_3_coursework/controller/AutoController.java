package ru.ekorzunov.urfu_bach_prog_3_coursework.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Auto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.AutoRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.service.UserDetailsServiceImpl;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/autos")
public class AutoController {

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/")
    public String getAutos() {
        return "redirect:./list";
    }

    @GetMapping("/list")
    public ModelAndView getAllAutos(@AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        List<Auto> autos;
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        if (is_admin) {
            autos = autoRepository.findAll();
        } else {
            User currentUser = userRepository.findByEmail(userDetail.getUsername());
            autos = currentUser.getAutos();
        }

        ModelAndView mav = new ModelAndView("list-autos");
        mav.addObject("autos", autos);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getAuto(@PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        User currentUser = userRepository.findByEmail(userDetail.getUsername());
        Auto auto = autoRepository.findById(id).orElseThrow();
        if (!(is_admin || auto.getOwner().equals(currentUser))) {
            throw new AccessDeniedException("403 returned");
        }
        ModelAndView mav = new ModelAndView("retrieve-auto");
        mav.addObject("auto", auto);
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addAutoForm(@AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("add-auto-form");

        boolean hideOwner;
        List<User> users;
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        if (is_admin) {
            users = userRepository.findAll();
            hideOwner = false;
        } else {
            User currentUser = userRepository.findByEmail(userDetail.getUsername());
            users = List.of(currentUser);
            hideOwner = true;
        }

        mav.addObject("auto", new Auto());
        mav.addObject("users", users);
        mav.addObject("hideOwner", hideOwner);
        return mav;
    }

    @PostMapping("/save")
    public String saveAuto(@ModelAttribute Auto auto, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        if (!is_admin) {
            User user = userRepository.findByEmail(userDetail.getUsername());
            auto.setOwner(user);
        }
        autoRepository.save(auto);
        return "redirect:./list";
    }

    @GetMapping("/update")
    public ModelAndView showUpdateForm(@RequestParam Long autoId, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("add-auto-form");

        // todo: throw 404
        Auto auto = autoRepository.findById(autoId).orElseThrow();

        List<User> users;
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        if (isAdmin) {
            users = userRepository.findAll();
        } else {
            User user = userRepository.findByEmail(userDetail.getUsername());
            users = List.of(user);
            boolean isOwner = auto.getOwner().getId() == user.getId();
            if (!isOwner) {
                throw new AccessDeniedException("403 returned");
            }
        }

        mav.addObject("auto", auto);
        mav.addObject("users", users);

        return mav;
    }

    @GetMapping("/delete")
    public String deleteAuto(@RequestParam Long autoId, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        User user = userRepository.findByEmail(userDetail.getUsername());
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        boolean is_owner = autoRepository.findById(autoId).orElseThrow().getOwner().equals(user);
        if (is_admin || is_owner) {
            autoRepository.deleteById(autoId);
        } else {
            throw new AccessDeniedException("403 returned");
        }
        return "redirect:./list";
    }

}
