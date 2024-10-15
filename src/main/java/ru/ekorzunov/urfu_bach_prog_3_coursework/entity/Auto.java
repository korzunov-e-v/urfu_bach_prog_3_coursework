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
@Table(name = "autos")
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "reg_number", nullable = false, unique = true)
    private String regNumber;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "color", nullable = false)
    private String color;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private User owner;

    @OneToMany(targetEntity = RentRecord.class, fetch = FetchType.EAGER, mappedBy = "auto",  cascade = CascadeType.ALL)
    private List<RentRecord> rentRecords = new ArrayList<>();

    @Override
    public String toString() {
        return "Auto{" + regNumber + " " + manufacturer + " " + model + " " + owner.getFullName() + "}";
    }
}
