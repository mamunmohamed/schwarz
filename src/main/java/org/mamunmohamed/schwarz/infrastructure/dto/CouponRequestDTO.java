package org.mamunmohamed.schwarz.infrastructure.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class CouponRequestDTO {
    @NonNull
    private List<String> codes;
}
