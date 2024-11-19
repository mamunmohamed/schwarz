package org.mamunmohamed.schwarz.domain.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class CouponRequest {
    @NonNull
    private List<String> codes;
}
