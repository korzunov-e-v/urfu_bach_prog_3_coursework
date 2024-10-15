package ru.ekorzunov.urfu_bach_prog_3_coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.UserAction;

import java.util.List;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    List<UserAction> findAllByUser(User owner);

}
