package org.mamunmohamed.schwarz.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ApplicationRequestDTO {

    @NotBlank
    private String code;

    @NotNull
    private BasketDTO basketDTO;

}
