package ru.ekorzunov.urfu_bach_prog_3_coursework.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

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

    private String password;

    private String adminToken;

    private List<Role> roles;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getRolesString() {
        return roles.stream().map(r -> r.toString().replace("ROLE_", "")).collect(Collectors.joining(", "));
    }

    public boolean isPasswordSet() {
        return !(password == null || password.isEmpty());
    }

}
