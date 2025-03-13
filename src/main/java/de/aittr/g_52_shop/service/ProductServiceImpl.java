package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductValidationException;
import de.aittr.g_52_shop.repository.ProductRepository;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import de.aittr.g_52_shop.service.mapping.ProductMappingService;
import jakarta.transaction.Transactional;
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

        try {
            Product entity = mappingService.mapDtoToEntity(dto);
            entity = repository.save(entity);
            return mappingService.mapEntityToDto(entity);
        } catch (Exception e) {
            throw new ProductValidationException(e);
        }
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
//        Product product = repository.findById(id).orElse(null);
//
//        if (product == null || !product.isActive()) {
//            throw new ProductNotFoundException(id);
//        }
//
//        return mappingService.mapEntityToDto(product);

        return mappingService.mapEntityToDto(getActiveProductEntityById(id));
    }

    public Product getActiveProductEntityById(Long id) {
        return repository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /*
    Аннотация @Transactional служить для того, чтобы транзакция, открытая в БД,
    действовала на протяжении всей работы метода.
    Таким образом, мы сохраняем наш продукт в состоянии managed, и все изменения, которые мы вносим
    в этот Java-объект, автоматически попадают в БД согласно концепции ORM.
    */
    @Override
    @Transactional
    public void update(ProductDto product) {
        Long id = product.getId();
        Product existentProduct = repository.findById(product.getId())
                .filter(Product::isActive)
                .orElseThrow(() -> new ProductNotFoundException(id));
        existentProduct.setPrice(product.getPrice());
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

    @Override
    @Transactional
    public void attachImage(String imageUrl, String productTitle) {
        repository.findByTitle(productTitle)
                .orElseThrow(() -> new ProductNotFoundException(productTitle))
                .setImage(imageUrl);
    }

    // https://shop-bucket.fra1.digitaloceanspaces.com
    // fKu8t3B3geeXdTJksnMAqR8tCtIghvXKwkMdHNCXrWc - secret key
    // DO801MMWX3RLJ7YYDHQJ - access key

}
