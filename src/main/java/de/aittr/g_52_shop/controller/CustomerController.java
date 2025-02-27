package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.entity.Customer;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    // Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным)
    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        return null;
    }

    // Вернуть всех покупателей из базы данных (активных)
    @GetMapping("/all")
    public List<Customer> getAll() {
        return null;
    }

    // Вернуть одного покупателя из базы данных по его идентификатору (если он активен)
    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return null;
    }

    // Изменить одного покупателя в базе данных по его идентификатору
    @PutMapping
    public void update(@RequestBody Customer customer) {
    }

    // Удалить покупателя из базы данных по его идентификатору
    @DeleteMapping("/{id}")
    public void deleteByID(@PathVariable Long id) {
    }

    // Удалить покупателя из базы данных по его имени
    @DeleteMapping("/by-title/{title}")
    public void deleteByName(@PathVariable String name) {

    }

    // Восстановить удалённого покупателя в базе данных по его идентификатору.
    @PutMapping("/restore/{id}")
    public void restoreById(@PathVariable Long id) {

    }

    // Вернуть общее количество покупателей в базе данных (активных)
    @GetMapping("/quantity")
    public long getCustomersQuantity() {
        return 0;
    }

    // Вернуть стоимость корзины покупателя по его идентификатору (если он активен)
    // TODO: написать после разработки класса корзины

    // Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
    // TODO: написать после разработки класса корзины

    // Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    // TODO: написать после разработки класса корзины

    // Удалить товар из корзины покупателя по их идентификаторам
    // TODO: написать после разработки класса корзины

    // Полностью очистить корзину покупателя по его идентификатору (если он активен)
    // TODO: написать после разработки класса корзины

}