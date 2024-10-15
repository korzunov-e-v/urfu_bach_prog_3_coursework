package ru.ekorzunov.urfu_bach_prog_3_coursework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @JoinTable(
            name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(targetEntity = UserAction.class, mappedBy = "user")
    private List<UserAction> actions = new ArrayList<>();

    @OneToMany(targetEntity = Auto.class, fetch = FetchType.EAGER, mappedBy = "owner")
    @Fetch(FetchMode.JOIN)
    private List<Auto> autos = new ArrayList<>();

    @OneToMany(targetEntity = RentRecord.class, fetch = FetchType.LAZY, mappedBy = "user")
    @Fetch(FetchMode.JOIN)
    private List<RentRecord> rentRecords = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" + id + ", " + email + ", " + getFullName() + '}';
    }
}
