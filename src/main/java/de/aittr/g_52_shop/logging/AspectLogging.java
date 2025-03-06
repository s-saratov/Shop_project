package de.aittr.g_52_shop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

// Аннотация @Aspect определяет, что этот класс является аспектом, т.е. классом, содержащим советы (адвайсы)
// Адвайсы - это методы, код которых внедряется в исходные методы приложения
// Аннотация @Component говорит Spring о том, что нужно создать объект этого класса и сделать его Bean-ом,
// поместив в Spring-контекст
@Aspect
@Component
public class AspectLogging {

    private Logger logger = LoggerFactory.getLogger(AspectLogging.class);

    // Аннотация @Pointcut служит для создания среза, т.е. правил, описывающих, куда именно
    // будет внедряться дополнительный код
    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.save(de.aittr.g_52_shop.domain.dto.ProductDto))")
    public void saveProduct() {}

    // Аннотация @Before говорит о том, что этот метоод является before-адвайсом,
    // т.е. он будет отрабатывать до основного кода
    // При этом при помощи "saveProduct()" мы указываем, к какому именно Pointcut-у
    // мы привязываем этот адвайс
    // jointPoint - это специальный объект, который создаётся фреймворком, и в него
    // закладывается вся информация о вызванном целевом методе
    @Before("saveProduct()")
    public void beforeSavingProduct(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        logger.info("Method save of the class ProductServiceImpl called with argument {}", args[0]);
    }

    @After("saveProduct()")
    public void afterSavingProduct() {
        logger.info("Method save of the class ProductServiceImpl finished its work");
    }

    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.getById(Long))")
    public void getProductById() {}

    @AfterReturning(
            pointcut = "getProductById()",
            returning = "result"
    )
    public void afterReturningProductById(Object result) {
        logger.info("Method getById of the ProductServiceImpl successfully returned product: {}", result);
    }


    @AfterThrowing(
            pointcut = "getProductById()",
            throwing = "e"
    )
    public void afterThrowingExceptionWhileGettingProduct(Exception e) {
        logger.warn("Method getById of the ProductServiceImpl threw an exception: {}", e.getMessage());
    }
}
