package org.mamunmohamed.schwarz.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CouponDTO {

    private BigDecimal discount;

    private String code;

    private BigDecimal minBasketValue;

}
