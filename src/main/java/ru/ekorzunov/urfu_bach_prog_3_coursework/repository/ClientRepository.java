package ru.ekorzunov.urfu_bach_prog_3_coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
