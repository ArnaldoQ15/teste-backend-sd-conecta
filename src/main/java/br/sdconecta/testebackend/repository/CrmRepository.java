package br.sdconecta.testebackend.repository;

import br.sdconecta.testebackend.model.Crm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrmRepository extends JpaRepository<Crm, Long> {

    @Query(value = "SELECT c FROM Crm c WHERE LOWER(c.specialty) LIKE :specialty%")
    Page<Crm> findBySpecialty(String specialty, Pageable pageRequest);

    @Query(value = "SELECT c FROM Crm c JOIN User u ON u.userId = c.user WHERE LOWER(c.specialty) LIKE :specialty% " +
            "AND u.userId = :userId")
    Page<Crm> findBySpecialtyAndUserId(String specialty, Long userId, Pageable pageRequest);

    @Query(value = "SELECT c FROM Crm c JOIN User u ON u.userId = c.user WHERE u.userId = :userId")
    Page<Crm> findByUser_Id(Long userId, Pageable pageRequest);

    @Query(value = "SELECT c FROM Crm c JOIN User u ON u.userId = c.user WHERE u.userId = :userId")
    List<Crm> findByUserId(Long userId);

}