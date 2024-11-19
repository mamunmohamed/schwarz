package org.mamunmohamed.schwarz.application;

import lombok.extern.slf4j.Slf4j;
import org.mamunmohamed.schwarz.domain.domain.Basket;
import org.mamunmohamed.schwarz.domain.domain.Coupon;
import org.mamunmohamed.schwarz.domain.domain.CouponRequest;
import org.mamunmohamed.schwarz.infrastructure.entity.CouponEntity;
import org.mamunmohamed.schwarz.infrastructure.mapper.CouponMapper;
import org.mamunmohamed.schwarz.infrastructure.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CouponService {

    private CouponRepository couponRepository;
    private CouponMapper couponMapper;

    public Optional<CouponEntity> getCoupon(final String code) {
        return couponRepository.findByCode(code);
    }

    public Optional<Basket> applyCoupon(final Basket basket, final String code) {

        return getCoupon(code).map(coupon -> {

            if (basket.getValue().doubleValue() >= 0) {

                if (basket.getValue().doubleValue() > 0) {

                    basket.applyDiscount(coupon.getDiscount());

                } else {
                    return basket;
                }
            } else {
                log.debug("DEBUG: TRIED TO APPLY NEGATIVE DISCOUNT!");
                throw new RuntimeException("Can't apply negative discounts");
            }

            return basket;
        });
    }

    public void createCoupon(final Coupon coupon) {

        if (coupon.getCode() == null){
            throw new IllegalArgumentException("Coupon must have a code");
        }
        couponRepository.save(couponMapper.toCouponEntity(coupon));
    }

    public List<Coupon> getCoupons(final CouponRequest couponRequest) {

        if (couponRequest == null || couponRequest.getCodes().isEmpty()) {
            throw new IllegalArgumentException("Coupon code must not be empty or null");
        }

        List<CouponEntity> coupons = couponRepository.findCouponsByCodeIn(couponRequest.getCodes());
        if (coupons.isEmpty()) {
            throw new IllegalArgumentException("No coupons found for the given code");
        }
        return couponMapper.fromCouponEntityListToCouponList(coupons);

    }

}
