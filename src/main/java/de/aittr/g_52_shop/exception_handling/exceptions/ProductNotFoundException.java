package de.aittr.g_52_shop.exception_handling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
Второй способ обработки исключений

Он заключается в том, что на сам класс исключения мы вешаем аннотацию @ResponseStatus
и указываем, какой именно статус ответа должен получить клиент, если у нас выброшено это исключение.

ПЛЮС    - очень легко и быстро создаём обработчик исключений вообще без создания каких-либо методов.
        Отлично подходить для простейших случаев.
МИНУС   - мы не можем отправить клиенту какое-то информативное сообщение, поэтому в более сложных ситуациях
        клиент не может понять причину ошибки, т.к. не видит её детального описания

 */

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super(String.format("Product with id %s not found", id));
    }

}