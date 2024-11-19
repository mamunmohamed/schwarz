package org.mamunmohamed.schwarz.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BasketDTO {
    private BigDecimal value;
    private String appliedDiscount;
    private boolean applicationSuccessful;
}
