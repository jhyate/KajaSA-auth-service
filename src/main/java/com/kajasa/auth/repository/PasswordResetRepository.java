package com.kajasa.auth.repository;

import com.kajasa.auth.entity.PasswordResetEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar los registros de recuperación
 * de contraseña de los usuarios.
 * Permite consultar y administrar los tokens de recuperación
 * asociados a cada usuario.
 */
@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity, UUID> {

    Optional<PasswordResetEntity> findByUserId(UUID userId);

    Optional<PasswordResetEntity> findByTokenHash(String tokenHash);

}
