package ru.ekorzunov.urfu_bach_prog_3_coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.RentRecord;

import java.util.List;

@Repository
public interface RentRecordRepository extends JpaRepository<RentRecord, Long> {

    List<RentRecord> findAllByUserId(long id);

}
