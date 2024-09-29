package ru.ekorzunov.urfu_bach_prog_3_coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.Auto;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Long> {
}
