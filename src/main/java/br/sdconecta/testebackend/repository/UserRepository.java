package br.sdconecta.testebackend.repository;

import br.sdconecta.testebackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail (String email);

    @Query(value = "SELECT u FROM User u WHERE LOWER(u.name) LIKE %:name%")
    Page<User> findByName(String name, Pageable pageRequest);

    @Query(value = "SELECT u FROM User u WHERE LOWER(u.email) LIKE %:email%")
    Optional<User> findByEmail(String email);

}
