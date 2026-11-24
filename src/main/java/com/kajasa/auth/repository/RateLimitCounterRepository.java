package com.kajasa.auth.repository;

import com.kajasa.auth.entity.RateLimitCounterEntity;
import com.kajasa.auth.enums.RateLimitCounterAction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio encargado de gestionar los contadores de solicitudes
 * utilizados para aplicar límites de frecuencia por usuario y acción.
 * Permite consultar y actualizar las ventanas de tiempo y cantidades
 * de solicitudes registradas.
 */
@Repository
public interface RateLimitCounterRepository extends JpaRepository<RateLimitCounterEntity, UUID> {

    Optional<RateLimitCounterEntity> findByUserIdAndAction(UUID userId, RateLimitCounterAction action);

}
