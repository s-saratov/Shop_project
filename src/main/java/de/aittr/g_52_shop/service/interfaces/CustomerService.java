package de.aittr.g_52_shop.service.interfaces;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {

    // Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным)
    CustomerDto save(CustomerDto customer);

    // Вернуть всех покупателей из базы данных (активных)
    List<CustomerDto> getAllActiveCustomers();

    // Вернуть одного покупателя из базы данных по его идентификатору (если он активен)
    CustomerDto getActiveCustomerById(Long id);

    // Изменить одного покупателя в базе данных по его идентификатору
    void update(CustomerDto customer);

    // Удалить покупателя из базы данных по его идентификатору
    void deleteByID(Long id);

    // Удалить покупателя из базы данных по его имени
    void deleteByName(String name);

    // Восстановить удалённого покупателя в базе данных по его идентификатору.
    void restoreById(Long id);

    // Вернуть общее количество покупателей в базе данных (активных)
    long getAllActiveCustomersNumber();

    // Вернуть стоимость корзины покупателя по его идентификатору (если он активен)
    BigDecimal getCustomersCartTotalCost(Long customerId);

    // Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
    BigDecimal getCustomersCartAveragePrice(Long customerId);

    // Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    void addProductToCustomerCart(Long customerId, Long productId);

    // Удалить товар из корзины покупателя по их идентификаторам
    void removeProductFromCustomersCart(Long customerId, Long productId);

    // Полностью очистить корзину покупателя по его идентификатору (если он активен)
    void clearCustomerCart(Long customerId);
}