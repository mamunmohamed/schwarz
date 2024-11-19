package org.mamunmohamed.schwarz.infrastructure.mapper;

import org.mamunmohamed.schwarz.domain.domain.Coupon;
import org.mamunmohamed.schwarz.domain.domain.CouponRequest;
import org.mamunmohamed.schwarz.infrastructure.dto.CouponDTO;
import org.mamunmohamed.schwarz.infrastructure.dto.CouponRequestDTO;
import org.mamunmohamed.schwarz.infrastructure.entity.CouponEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    Coupon toCoupon(CouponDTO couponDTO);

    CouponEntity toCouponEntity (Coupon coupon);

    List<CouponDTO> toCouponDTO(List<Coupon> coupons);

    List<Coupon> fromCouponEntityListToCouponList (List<CouponEntity> couponEntities);

    CouponRequest toCouponRequest(CouponRequestDTO couponRequestDTO);


}
