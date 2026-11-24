package com.kajasa.auth.repository;

import com.kajasa.auth.entity.TokenFamilyEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar las familias de refresh tokens.
 * Permite consultar y administrar las familias asociadas a las sesiones,
 * incluyendo su estado, expiración y revocación.
 */
@Repository
public interface TokenFamilyRepository extends JpaRepository<TokenFamilyEntity, UUID> {

    Optional<TokenFamilyEntity> findBySessionId(UUID sessionId);

}
