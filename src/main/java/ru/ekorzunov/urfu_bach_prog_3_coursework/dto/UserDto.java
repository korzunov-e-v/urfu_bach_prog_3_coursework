package ru.ekorzunov.urfu_bach_prog_3_coursework.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty(message = "Email should not be empty.")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty.")
    private String password;

    private String adminToken;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
