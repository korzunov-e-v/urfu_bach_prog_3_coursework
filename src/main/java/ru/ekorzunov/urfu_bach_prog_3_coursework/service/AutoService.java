package ru.ekorzunov.urfu_bach_prog_3_coursework.service;

import org.springframework.stereotype.Service;
import ru.ekorzunov.urfu_bach_prog_3_coursework.dto.AutoDto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Auto;

import java.util.List;

@Service
public interface AutoService {

    void saveAuto(AutoDto AutoDto);

    Auto findAutoByEmail(String email);

    List<AutoDto> findAllAutos();

}
