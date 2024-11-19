package org.mamunmohamed.schwarz.infrastructure.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mamunmohamed.schwarz.application.CouponService;
import org.mamunmohamed.schwarz.domain.domain.Basket;
import org.mamunmohamed.schwarz.domain.domain.CouponRequest;
import org.mamunmohamed.schwarz.infrastructure.dto.ApplicationRequestDTO;
import org.mamunmohamed.schwarz.infrastructure.dto.BasketDTO;
import org.mamunmohamed.schwarz.infrastructure.dto.CouponDTO;
import org.mamunmohamed.schwarz.infrastructure.dto.CouponRequestDTO;
import org.mamunmohamed.schwarz.infrastructure.mapper.BasketMapper;
import org.mamunmohamed.schwarz.infrastructure.mapper.CouponMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CouponResource {

    private final CouponService couponService;

    private BasketMapper basketMapper;

    private CouponMapper couponMapper;

    @PostMapping("/apply")
    public ResponseEntity<BasketDTO> applyCoupon(@RequestBody @Valid final ApplicationRequestDTO applicationRequestDTO){
        final Optional<Basket> basket =  couponService.applyCoupon(basketMapper.toBasket(applicationRequestDTO.getBasketDTO()), applicationRequestDTO.getCode());

        log.info("Applying coupon");

        if (basket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BasketDTO basketDTO =  basketMapper.toBasketDTO(basket);
        if (!basketDTO.isApplicationSuccessful()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        log.info("Applied coupon");

        return ResponseEntity.ok().body(basketDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createCoupon(@RequestBody @Valid final CouponDTO couponDTO){
        couponService.createCoupon(couponMapper.toCoupon(couponDTO));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/coupons")
    public List<CouponDTO> getCoupons(@RequestBody @Valid final CouponRequestDTO couponRequestDTO) {
        CouponRequest couponRequest =   couponMapper.toCouponRequest(couponRequestDTO);
        return couponMapper.toCouponDTO(couponService.getCoupons(couponRequest));
    }
}
