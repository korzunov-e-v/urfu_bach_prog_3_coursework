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
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.RentRecord;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.AutoRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.RentRecordRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/rentrecords")
public class RentController {

    @Autowired
    private RentRecordRepository rentrecordRepository;

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getRentRecords() {
        return "redirect:./list";
    }

    @GetMapping("/list")
    public ModelAndView getAllRentRecords(@AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        List<RentRecord> rentRecords;
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        if (is_admin) {
            rentRecords = rentrecordRepository.findAll();
        } else {
            User currentUser = userRepository.findByEmail(userDetail.getUsername());
            rentRecords = currentUser.getRentRecords();
        }

        ModelAndView mav = new ModelAndView("list-rentrecords");
        mav.addObject("rentrecords", rentRecords);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getRentRecord(@PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        User currentUser = userRepository.findByEmail(userDetail.getUsername());
        RentRecord rentrecord = rentrecordRepository.findById(id).orElseThrow();
        if (!(is_admin || rentrecord.getUser().getId() == (currentUser.getId()))) {
            throw new AccessDeniedException("403 returned");
        }
        ModelAndView mav = new ModelAndView("retrieve-rentrecord");
        mav.addObject("rentrecord", rentrecord);
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addRentRecordForm(@AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("add-rentrecord-form");

        boolean hideOwner;
        List<User> users;
        List<Auto> autos;
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        if (is_admin) {
            users = userRepository.findAll();
            autos = autoRepository.findAll();
            hideOwner = false;
        } else {
            User currentUser = userRepository.findByEmail(userDetail.getUsername());
            users = List.of(currentUser);
            autos = autoRepository.findAllByOwner(currentUser);
            hideOwner = true;
        }

        mav.addObject("rentrecord", new RentRecord());
        mav.addObject("users", users);
        mav.addObject("autos", autos);
        mav.addObject("hideOwner", hideOwner);
        return mav;
    }

    @PostMapping("/save")
    public String saveRentRecord(@ModelAttribute RentRecord rentrecord, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        if (!is_admin) {
            User user = userRepository.findByEmail(userDetail.getUsername());
            rentrecord.setUser(user);
            boolean isOwner = rentrecord.getAuto().getOwner().getId() == user.getId();
            if (!isOwner) {
                throw new AccessDeniedException("403 returned");
            }
        }
        rentrecordRepository.save(rentrecord);
        return "redirect:./list";
    }

    @GetMapping("/update")
    public ModelAndView showUpdateForm(@RequestParam Long rentrecordId, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("add-rentrecord-form");

        RentRecord rentrecord = rentrecordRepository.findById(rentrecordId).orElseThrow();

        List<User> users;
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        if (isAdmin) {
            users = userRepository.findAll();
        } else {
            User user = userRepository.findByEmail(userDetail.getUsername());
            users = List.of(user);
            boolean isOwner = rentrecord.getUser().getId() == user.getId();
            if (!isOwner) {
                throw new AccessDeniedException("403 returned");
            }
        }

        mav.addObject("rentrecord", rentrecord);
        mav.addObject("users", users);

        return mav;
    }

    @GetMapping("/delete")
    public String deleteRentRecord(@RequestParam Long rentrecordId, @AuthenticationPrincipal UserDetails userDetail, HttpServletRequest request) {
        User user = userRepository.findByEmail(userDetail.getUsername());
        boolean is_admin = request.isUserInRole("ROLE_ADMIN");
        boolean is_owner = rentrecordRepository.findById(rentrecordId).orElseThrow().getUser().getId() == user.getId();
        if (is_admin || is_owner) {
            rentrecordRepository.deleteById(rentrecordId);
        } else {
            throw new AccessDeniedException("403 returned");
        }
        return "redirect:./list";
    }

}
