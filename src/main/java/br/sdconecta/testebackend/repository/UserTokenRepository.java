package br.sdconecta.testebackend.repository;

import br.sdconecta.testebackend.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query(value = "SELECT ut FROM UserToken ut JOIN User u ON u.token = ut.tokenId WHERE u.email = :email")
    Optional<UserToken> findByEmail(String email);

}
