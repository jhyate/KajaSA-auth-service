package com.kajasa.auth.repository;

import com.kajasa.auth.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar las operaciones de persistencia
 * y consulta de usuarios registrados en KajaSA.
 * Permite acceder a los usuarios mediante su identificador
 * y dirección de correo electrónico.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
