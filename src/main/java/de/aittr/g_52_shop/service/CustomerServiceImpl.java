package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Cart;
import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.g_52_shop.repository.CustomerRepository;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import de.aittr.g_52_shop.service.mapping.CustomerMappingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMappingService mappingService;
    private final ProductService productService;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMappingService mappingService, ProductService productService) {
        this.repository = repository;
        this.mappingService = mappingService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public CustomerDto save(CustomerDto dto) {
        Customer entity = mappingService.mapDtoToEntity(dto);

        Cart cart = new Cart();
        cart.setCustomer(entity);
        entity.setCart(cart);

        entity = repository.save(entity);
        return mappingService.mapEntityToDto(entity);
    }

    @Override
    public List<CustomerDto> getAllActiveCustomers() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CustomerDto getActiveCustomerById(Long id) {
        Customer customer = repository.findById(id).orElse(null);

        if (customer == null || !customer.isActive()) {
            return null;
        }

        return mappingService.mapEntityToDto(customer);
    }


    private Customer getActiveCustomerEntityById(Long id) {
        return repository.findById(id)
                .filter(Customer::isActive)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public void update(CustomerDto customer) {

    }

    @Override
    public void deleteByID(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveCustomersNumber() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .count();
    }

    @Override
    public BigDecimal getCustomersCartTotalCost(Long customerId) {
        return null;
    }

    @Override
    public BigDecimal getCustomersCartAveragePrice(Long customerId) {
        return null;
    }

    @Override
    public void addProductToCustomerCart(Long customerId, Long productId) {
        Customer customer = getActiveCustomerEntityById(customerId);
        Product product = productService.getActiveProductEntityById(productId);
        customer.getCart().addProduct(product);
    }

    @Override
    public void removeProductFromCustomersCart(Long customerId, Long productId) {

    }

    @Override
    public void clearCustomerCart(Long customerId) {

    }
}