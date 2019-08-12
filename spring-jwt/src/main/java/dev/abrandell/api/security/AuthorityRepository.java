package dev.abrandell.api.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(rollbackFor = Exception.class)
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("SELECT a "
        + "FROM Authority a "
        + "WHERE upper(a.authority) = upper(:authority)")
    @Transactional(readOnly = true)
    Optional<Authority> fetchByName(@Param("authority") final String authority);
}
