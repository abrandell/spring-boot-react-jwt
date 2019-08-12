package dev.abrandell.api.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional(rollbackFor = Exception.class)
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a "
        + "FROM Account a "
        + "JOIN FETCH a.authorities "
        + "WHERE a.username = :username")
    @Transactional(readOnly = true)
    Optional<Account> fetchByUsername(@Param("username") final String username);
}
