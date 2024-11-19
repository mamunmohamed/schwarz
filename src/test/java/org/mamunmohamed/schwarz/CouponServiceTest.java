package org.mamunmohamed.schwarz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mamunmohamed.schwarz.application.CouponService;
import org.mamunmohamed.schwarz.domain.domain.Basket;
import org.mamunmohamed.schwarz.domain.domain.Coupon;
import org.mamunmohamed.schwarz.domain.domain.CouponRequest;
import org.mamunmohamed.schwarz.infrastructure.entity.CouponEntity;
import org.mamunmohamed.schwarz.infrastructure.mapper.CouponMapper;
import org.mamunmohamed.schwarz.infrastructure.repository.CouponRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper couponMapper;

    @Test
    public void createCoupon() {
        Coupon coupon = Coupon.builder()
                .code("12345")
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(50))
                .build();

        couponService.createCoupon(coupon);

        verify(couponRepository, times(1)).save(any());
    }

    @Test
    public void test_apply_coupon_method() {

        final Basket firstBasket = Basket.builder()
                .value(BigDecimal.valueOf(100))
                .build();

        when(couponRepository.findByCode("1111"))
                .thenReturn(Optional.of(CouponEntity.builder()
                .code("1111")
                .discount(BigDecimal.TEN)
                .minBasketValue(BigDecimal.valueOf(50))
                .build()));

        Optional<Basket> optionalBasket = couponService.applyCoupon(firstBasket, "1111");

        assertThat(optionalBasket).hasValueSatisfying(b -> {
            assertThat(b.getAppliedDiscount()).isEqualTo(BigDecimal.TEN);
            assertThat(b.isApplicationSuccessful()).isTrue();
        });

        final Basket secondBasket = Basket.builder()
                .value(BigDecimal.valueOf(0))
                .build();

        optionalBasket = couponService.applyCoupon(secondBasket, "1111");

        assertThat(optionalBasket).hasValueSatisfying(b -> {
            assertThat(b).isEqualTo(secondBasket);
            assertThat(b.isApplicationSuccessful()).isFalse();
        });

        final Basket thirdBasket = Basket.builder()
                .value(BigDecimal.valueOf(-1))
                .build();

        assertThatThrownBy(() -> {
            couponService.applyCoupon(thirdBasket, "1111");
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Can't apply negative discounts");
    }

    @Test
    public void should_test_get_Coupons() {

        CouponRequest dto = CouponRequest.builder()
                .codes(Arrays.asList("1111", "1234"))
                .build();

        when(couponRepository.findByCode(any()))
                .thenReturn(Optional.of(CouponEntity.builder()
                        .code("1111")
                        .discount(BigDecimal.TEN)
                        .minBasketValue(BigDecimal.valueOf(50))
                        .build()))
                .thenReturn(Optional.of(CouponEntity.builder()
                        .code("1234")
                        .discount(BigDecimal.TEN)
                        .minBasketValue(BigDecimal.valueOf(50))
                        .build()));

        List<Coupon> returnedCoupons = couponService.getCoupons(dto);

        assertThat(returnedCoupons.get(0).getCode()).isEqualTo("1111");

        assertThat(returnedCoupons.get(1).getCode()).isEqualTo("1234");
    }
}

