package de.aittr.g_52_shop.service.interfaces;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {

    // Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным)
    Customer save(@RequestBody Customer customer);

    // Вернуть всех покупателей из базы данных (активных)
    List<Customer> getAllActiveCustomers();

    // Вернуть одного покупателя из базы данных по его идентификатору (если он активен)
    Customer getById(@PathVariable Long id);

    // Изменить одного покупателя в базе данных по его идентификатору
    void update(@RequestBody Customer customer);

    // Удалить покупателя из базы данных по его идентификатору
    void deleteByID(@PathVariable Long id);

    // Удалить покупателя из базы данных по его имени
    void deleteByName(@PathVariable String name);

    // Восстановить удалённого покупателя в базе данных по его идентификатору.
    void restoreById(@PathVariable Long id);

    // Вернуть общее количество покупателей в базе данных (активных)
    long getAllActiveCustomersCount();

    // Вернуть стоимость корзины покупателя по его идентификатору (если он активен)
    // TODO: написать после разработки класса корзины

    // Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
    // TODO: написать после разработки класса корзины

    // Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    // TODO: написать после разработки класса корзины

    // Удалить товар из корзины покупателя по их идентификаторам
    // TODO: написать после разработки класса корзины
}