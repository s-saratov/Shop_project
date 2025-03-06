package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.repository.ProductRepository;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import de.aittr.g_52_shop.service.mapping.ProductMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/*
Аннотация @Service говорит Spring о том, что при старте приложения нужно создать объект этого класса
и поместить его в Spring-контекст.
Кроме того, данная аннотация носит информационный характер, она говорит нам о том, что
перед нами класс сервиса.
 */

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    // Этот сервис мы будем вызывать, когда нам понадобится сконвертировать Product в DTO и наоборот
    private final ProductMappingService mappingService;

    // Это объект логгера, с помощью него осуществляется логгирование
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /*
    Когда Spring будет создавать объект сервиса продуктов, он вызовет этот конструктор (потому что
    других вариантов нет), и в этот конструктор требуется передать объект репозитория.
    Поэтому Spring обратится в контекст, получит оттуда репозиторий и передаст в этот параметр.
    А объект репозитория уже будет находиться там благодаря наследованию нашего интерфейса репозитория
    от JpaRepository.
     */

    public ProductServiceImpl(ProductRepository repository, ProductMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public ProductDto save(ProductDto dto) {
        Product entity = mappingService.mapDtoToEntity(dto);
        entity = repository.save(entity);
        System.out.println(" ***** РАБОТАЕТ МЕТОД SAVE *****");
        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<ProductDto> getAllActiveProducts() {

        // При помощи разных методов объекта логгера мы можем фиксировать события,
        // происходящие в программе, на разные уровни
//        logger.info("Request for all products received");
//        logger.warn("Request for all products received");
//        logger.error("Request for all products received");

        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = repository.findById(id).orElse(null);

        if (product == null || !product.isActive()) {
            throw new RuntimeException("Product not found");
        }

        return mappingService.mapEntityToDto(product);
    }

    @Override
    public void update(ProductDto product) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByTitle(String title) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveProductsCount() {
//        return repository.findAll()
//                .stream()
//                .filter(Product::isActive)
//                .count();

//        return getAllActiveProducts().size();

        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .count();
    }

    @Override
    public BigDecimal getAllActiveProductsTotalCost() {
        return null;
    }

    @Override
    public BigDecimal getAllActiveProductsAveragePrice() {
        return null;
    }
}
