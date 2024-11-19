package org.mamunmohamed.schwarz.infrastructure.repository;

import org.mamunmohamed.schwarz.infrastructure.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponEntity, Long>{
    Optional<CouponEntity> findByCode(final String code);

    @Query("SELECT c FROM CouponEntity c WHERE c.code IN:codes")
    List<CouponEntity> findCouponsByCodeIn(@Param("codes")final List<String> codes);
}
