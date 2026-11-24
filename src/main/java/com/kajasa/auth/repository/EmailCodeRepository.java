package com.kajasa.auth.repository;

import com.kajasa.auth.entity.EmailCodeEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar los códigos de verificación
 * del correo electrónico de los usuarios.
 * Permite consultar y administrar el registro de verificación
 * asociado a cada usuario.
 */
@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCodeEntity, UUID> {

    Optional<EmailCodeEntity> findByUserId(UUID userId);

}
