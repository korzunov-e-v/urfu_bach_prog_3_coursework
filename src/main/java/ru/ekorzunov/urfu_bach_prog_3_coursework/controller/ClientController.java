package ru.ekorzunov.urfu_bach_prog_3_coursework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Client;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.ClientRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public ModelAndView getAllClients() {
        log.info("/list -> connection");
        List<Client> clients = clientRepository.findAll().stream().toList();
        ModelAndView mav = new ModelAndView("list-clients");
        mav.addObject("clients", clients);
        return mav;
    }

    @GetMapping("/addClientForm")
    public ModelAndView addClientForm() {
        ModelAndView mav = new ModelAndView("add-client-form");
        Client client = new Client();
        mav.addObject("client", client);
        return mav;
    }

    @PostMapping("/saveClient")
    public String saveClient(@ModelAttribute Client client) {
        clientRepository.save(client);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long clientId) {
        ModelAndView mav = new ModelAndView("add-client-form");
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        Client client = new Client();
        if (optionalClient.isPresent()) {
            client = optionalClient.get();
        }
        mav.addObject("client", client);

        List<User> allUsers = userRepository.findAll().stream().toList();
        mav.addObject("users", allUsers);

        return mav;
    }

    @GetMapping("/deleteClient")
    public String deleteClient(@RequestParam Long clientId) {
        clientRepository.deleteById(clientId);
        return "redirect:/list";
    }





}
