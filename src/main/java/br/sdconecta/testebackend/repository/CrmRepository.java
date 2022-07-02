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

    @Query(value = "SELECT c FROM Crm c WHERE LOWER(c.specialty) LIKE %:specialty%")
    Page<Crm> findBySpecialty(String specialty, Pageable pageRequest);

    @Query(value = "SELECT * FROM CRM c JOIN USER_SYSTEM u ON u.USER_ID = c.USER_ID WHERE LOWER(c.specialty) = %:specialty% " +
            "AND c.USER_ID = :userId", nativeQuery = true)
    Page<Crm> findBySpecialtyAndUserId(String specialty, Long userId, Pageable pageRequest);

    @Query(value = "SELECT * FROM CRM c JOIN USER_SYSTEM u ON u.USER_ID = c.USER_ID WHERE c.USER_ID = :userId", nativeQuery = true)
    Page<Crm> findByUser_Id(Long userId, Pageable pageRequest);

    @Query(value = "SELECT * FROM CRM c JOIN USER_SYSTEM US on US.USER_ID = c.USER_ID " +
            "WHERE c.USER_ID = :userId", nativeQuery = true)
    List<Crm> findByUserId(Long userId);

}