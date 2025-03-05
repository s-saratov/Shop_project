package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.CartDto;
import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Cart;
import de.aittr.g_52_shop.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductMappingService.class)
public interface CartMappingService {

    CustomerDto mapDtoToDto(Customer customer);

    CartDto mapEntityToDto(Cart entity);

    Cart mapDtoToEntity(CartDto dto);

}