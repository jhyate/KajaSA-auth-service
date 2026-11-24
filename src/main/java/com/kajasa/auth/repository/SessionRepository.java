package com.kajasa.auth.repository;

import com.kajasa.auth.entity.SessionEntity;
import com.kajasa.auth.enums.SessionStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar las sesiones de los usuarios.
 * Permite consultar, administrar y controlar el estado de las sesiones,
 * incluyendo su expiración y revocación.
 */
@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {

    Optional<SessionEntity> findBySessionTokenHash(String sessionTokenHash);

    Optional<SessionEntity> findBySessionTokenHashAndStatus(String sessionTokenHash, SessionStatus status);

    List<SessionEntity> findAllByUserIdAndStatus(UUID userId, SessionStatus status);
}
