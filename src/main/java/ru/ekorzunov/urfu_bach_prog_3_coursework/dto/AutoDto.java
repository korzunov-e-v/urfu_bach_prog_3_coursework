package ru.ekorzunov.urfu_bach_prog_3_coursework.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutoDto {

    private long id;

    @NotEmpty
    private String regNumber;

    @NotEmpty
    private String manufacturer;

    @NotEmpty
    private String model;

    @NotEmpty
    private int year;

    @NotEmpty
    private String color;

    @NotEmpty
    private User owner;

}
