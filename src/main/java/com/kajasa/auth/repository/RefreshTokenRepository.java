package com.kajasa.auth.repository;

import com.kajasa.auth.entity.RefreshTokenEntity;
import com.kajasa.auth.enums.RefreshTokenStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar los refresh tokens de los usuarios.
 * Permite consultar y administrar los tokens asociados a una familia,
 * facilitando su rotación, revocación y detección de reutilización.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    List<RefreshTokenEntity> findAllByFamilyId(UUID familyId);

    List<RefreshTokenEntity> findByFamilyIdAndStatus(UUID familyId, RefreshTokenStatus status);
}
