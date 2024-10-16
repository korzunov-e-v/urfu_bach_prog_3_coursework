package ru.ekorzunov.urfu_bach_prog_3_coursework.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Auto;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentRecordDto {

    private long id;

    @NotEmpty
    private User user;

    @NotEmpty
    private Auto auto;

    @NotEmpty
    private Date timestampStart;

    private Date timestampEnd;

}
