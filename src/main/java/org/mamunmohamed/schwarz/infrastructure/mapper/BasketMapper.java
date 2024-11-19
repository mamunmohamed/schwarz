package org.mamunmohamed.schwarz.infrastructure.mapper;

import org.mamunmohamed.schwarz.domain.domain.Basket;
import org.mamunmohamed.schwarz.infrastructure.dto.BasketDTO;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface BasketMapper {

    Basket toBasket(BasketDTO basketDTO);

    BasketDTO toBasketDTO(Optional<Basket> basket);


}
