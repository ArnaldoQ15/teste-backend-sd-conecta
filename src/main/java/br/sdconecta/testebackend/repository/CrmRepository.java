package br.sdconecta.testebackend.repository;

import br.sdconecta.testebackend.model.Crm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmRepository extends JpaRepository<Crm, Long> {

    @Query(value = "SELECT c FROM Crm c WHERE LOWER(c.specialty) LIKE %:specialty%")
    Page<Crm> findBySpecialty(String specialty, Pageable pageRequest);

}
