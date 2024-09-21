package ru.ekorzunov.urfu_bach_prog_3_coursework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rent_records")
public class RentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "client", nullable = false)
    private Client client;

    @ManyToOne(targetEntity = Auto.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "auto", nullable = false)
    private Auto auto;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "timestamp_start", nullable = false)
    private Date timestamp_start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "timestamp_end", nullable = true)
    private Date timestamp_end;

}
