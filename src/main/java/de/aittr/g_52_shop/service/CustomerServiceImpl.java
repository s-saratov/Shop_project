package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.repository.CustomerRepository;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {
        customer.setActive(true);
        return repository.save(customer);
    }

    @Override
    public List<Customer> getAllActiveCustomers() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .toList();
    }

    @Override
    public Customer getById(Long id) {
        Customer customer = repository.findById(id).orElse(null);

        if (customer == null || !customer.isActive()) {
            return null;
        }

        return customer;
    }

    @Override
    public void update(Customer customer) {

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
    public long getAllActiveCustomersCount() {
        return getAllActiveCustomers().size();
    }
}
