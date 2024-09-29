package ru.ekorzunov.urfu_bach_prog_3_coursework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Auto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.AutoRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/autos")
public class AutoController {

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public ModelAndView getAllAutos() {
        List<Auto> autos = autoRepository.findAll().stream().toList();
        ModelAndView mav = new ModelAndView("list-autos");
        mav.addObject("autos", autos);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getStudent(@PathVariable("id") long id) {
        Auto auto = autoRepository.findById(id).orElseThrow();
        ModelAndView mav = new ModelAndView("retrieve-auto");
        mav.addObject("auto", auto);
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView addAutoForm() {
        ModelAndView mav = new ModelAndView("add-auto-form");
        Auto auto = new Auto();
        mav.addObject("auto", auto);
        return mav;
    }

    @PostMapping("/save")
    public String saveAuto(@ModelAttribute Auto auto, @AuthenticationPrincipal User user) {
        auto.setOwner(user);
        autoRepository.save(auto);
        return "redirect:./list";
    }

    @GetMapping("/update")
    public ModelAndView showUpdateForm(@RequestParam Long autoId) {
        ModelAndView mav = new ModelAndView("add-auto-form");
        Optional<Auto> optionalAuto = autoRepository.findById(autoId);
        Auto auto = new Auto();
        if (optionalAuto.isPresent()) {
            auto = optionalAuto.get();
        }
        mav.addObject("auto", auto);
        return mav;
    }

    @GetMapping("/delete")
    public String deleteAuto(@RequestParam Long autoId) {
        autoRepository.deleteById(autoId);
        return "redirect:./list";
    }

}
