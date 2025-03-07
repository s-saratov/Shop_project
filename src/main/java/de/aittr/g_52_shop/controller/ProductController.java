package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.exception_handling.Response;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNotFoundException;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/*
Аннотация @RestController позволяет Spring понять, что этот класс является именно REST-контроллером.
Также благодаря этой аннотации на старте приложения Spring сам создаёт объект этого класса
и помещает его в Spring-контекст. Java-объекты, которые создаются Spring-ом и хранятся
в Spring-контексте, называются Spring-бинами.

Аннотация @RestController со значением аттрибута "/products" говорит Spring о том, что все HTTP-запросы,
которые пришли на endpoint /products, нужно адресовать именно этому контроллеру.
Когда на наше приложение придёт HTTP-запрос на /products, Spring будет сам вызывать нужные методы
у этого контроллера.

IoC - Inversion of Control (инверсия контроля) - этот принцип говорит о том, что мы пишем только методы,
а управление этими методами ложится на плечи фреймворка.
Мы пишем только методы, а фреймворк создаёт сам нужные объекты наших классов, и сам в нужный момент
вызывает их методы.
 */

@RestController
@RequestMapping("/products")
@Tag(name = "Product controller", description = "Controller for various operations with Products")
public class ProductController {

    // Это поле будет содержать объект сервиса продуктов, чтобы мы могли в коде контроллера
    // вызывать его методы
    private final ProductService service;

    /*
    При старте приложения Spring будет создавать объект данного контроллера.
    При создании объекта контроллера Spring вызовет этот конструктор, потому что других вариантов
    создания объекта больше нет, конструктор у нас один.
    Этот конструктор требует на вход объект сервиса продуктов. Поэтому Spring обратится
    к Spring-контексту, найдёт там там объект сервиса и передаст его в это поле.
    Поэтому чтобы всё отработала, нам нужно обеспечить, чтобы объект сервиса вообще был в этом контексте.
     */

    public ProductController(ProductService service) {
        this.service = service;
    }

    // Разработаем REST-API для нашего приложения.
    // Разработать REST-API означает определить, на какие endpoint-ы должен обращаться клиент,
    // чтобы выполнить те или иные операции.

    // Сохранить продукт в базе данных (при сохранении продукт автоматически считается активным).
    // POST -> http://12.34.56.78:8800/products (продукт передаётся в теле в виде JSON)

    /*
    Аннотация @PostMapping говорит Spring о том, что когда для данного контроллера придёт POST-запрос
    на ресурс /products, нужно вызывать именно этот метод.

    Аннотация @RequestBody говорит Spring о том, что он должен прочитать JSON, который пришёл
    в теле запроса, этот JSON преобразовать в Java-объект при помощи встроенного Jackson,
    и передать получившийся Java-объект в параметр product
     */

    @PostMapping
    public ProductDto save(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Instance of a Product")
            ProductDto product) {
        return service.save(product);
    }


    // Вернуть все продукты из базы данных (активные).
    // GET -> http://12.34.56.78:8800/products/all
    @Operation(
            summary = "Get all products",
            description = "Getting all products that exist in the database"
    )
    @GetMapping("/all")
    public List<ProductDto> getAll() {
        return service.getAllActiveProducts();
    }

    // Вернуть один продукт из базы данных по его идентификатору (если он активен).
    // GET -> http://12.34.56.78:8800/products?id=5 - вариант при помощи указания параметра
    // GET -> http://12.34.56.78:8800/products/5 - вариант при помощи подстроки запроса

    /*
    Аннотация @PathVariable говорит Spring о том, что значение для этого параметра нужно получить
    из самой строки запроса. А из какой именно части, указывает атрибут "/{id}" аннотации @GetMapping.
    */

    // @GetMapping("/{id}/test/{title}") - пример аннотации с несколькими параметрами
    @GetMapping("/{id}")
    public ProductDto getById(
            @PathVariable
            @Parameter(description = "Product unique identifier")
            Long id
    ) {
        return service.getById(id);
    }

    // Изменить один продукт в базе данных по его идентификатору.
    // PUT -> http://12.34.56.78:8800/products (идентификатор отправляем в теле)
    @PutMapping
    public void update(@RequestBody ProductDto product) {
        service.update(product);
    }

    // Удалить продукт из базы данных по его идентификатору.
    // DELETE -> http://12.34.56.78:8800/products/5
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    // Удалить продукт из базы данных по его наименованию.
    // DELETE -> http://12.34.56.78:8800/products/by-title/banana - вариант № 1
    @DeleteMapping("/by-title/{title}")
    public void deleteByTitle(@PathVariable String title) {
        service.deleteByTitle(title);
    }

    // DELETE -> http://12.34.56.78:8800/products?title=banana - вариант № 2
    // При таком варианте использовалась бы аннотация @RequestParam

    // Восстановить удалённый продукт в базе данных по его идентификатору.
    // PUT -> http://12.34.56.78:8800/products/restore/5
    @PutMapping("/restore/{id}")
    public void restoreById(@PathVariable Long id) {
        service.restoreById(id);
    }

    // Вернуть общее количество продуктов в базе данных (активных).
    // GET -> http://12.34.56.78:8800/products/quantity
    @GetMapping("/quantity")
    public long getProductsQuantity() {
        return service.getAllActiveProductsCount();
    }

    // Вернуть суммарную стоимость всех продуктов в базе данных (активных).
    // GET -> http://12.34.56.78:8800/products/total-cost
    @GetMapping("/total-cost")
    public BigDecimal getTotalCost() {
        return service.getAllActiveProductsTotalCost();
    }

    // Вернуть среднюю стоимость продукта в базе данных (из активных).
    // GET -> http://12.34.56.78:8800/products/avg-price
    @GetMapping("/avg-price")
    public BigDecimal getProductAveragePrice() {
        return service.getAllActiveProductsAveragePrice();
    }

    /*
    Этот метод является обработчиком конкретных исключений типа ProductNotFoundException.
    На это указывает аннотация @ExceptionHandler.
    Как это работает:
    1. Исключение выбрасывается сервисом.
    2. Т.к. исключение в сервисе не обрабатывается, оно пробрасывается вызывающему коду - т.е. контроллеру.
    3. Т.к. в контроллере есть обработчик этого исключения, и Spring это видит благодаря аннотации,
    Spring перехватывает это исключение.
    4. Spring сам вызывает наш метод handleException, передавая в параметр e перехваченное исключение.
     */

    /*
    Первый способ обработки ошибок
    ПЛЮС    - мы точечно настраиваем обработчик ошибок именно для данного контроллера,
            если нам требуется разная логика обработки ошибок для разных контроллеров.
    МИНУС   - если нам не требуется разная логика обработки ошибок для разных контроллеров,
            то при таком подходе нам придётся во всех контроллерах создавать такие одинаковые обработчики
     */

//    @ExceptionHandler(ProductNotFoundException.class)
//    public Response handleException(ProductNotFoundException e) {
//        return new Response(e.getMessage());
//    }

}