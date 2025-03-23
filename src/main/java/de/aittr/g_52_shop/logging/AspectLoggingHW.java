package de.aittr.g_52_shop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLoggingHW {

    private Logger logger = LoggerFactory.getLogger(AspectLoggingHW.class);

    @Pointcut("execution(* de.aittr.g_52_shop.service..*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void beforeExecutingMethods(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String stringArgs = getArgs(args);
        logger.info("Method {} of class {} called with argument(s): [{}]",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                stringArgs);
    }

    @After("serviceMethods()")
    public void afterExecutingMethods(JoinPoint joinPoint) {
        logger.info("Method {} of class {} finished its work",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName());
    }

    @AfterReturning(
            pointcut = "serviceMethods()",
            returning = "result"
    )
    public void afterReturningMethods(JoinPoint joinPoint, Object result) {
        logger.info("Method {} of class {} successfully returned result: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                result);
    }

    @AfterThrowing(
            pointcut = "serviceMethods()",
            throwing = "e"
    )
    public void afterThrowingExceptionWhileGettingProduct(JoinPoint joinPoint, Exception e) {
        logger.warn("Method {} of class {} threw an exception: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                e.getMessage());
    }

    // Вспомогательные методы

    // Возвращает список аргументов функции в виде строки
    public String getArgs(Object[] args) {
        String argsString = "";
        for (int i = 0; i < args.length; i++) {
            argsString += args[i].toString();
            if (i != args.length - 1) {
                argsString += ", ";
            }
        }
        return argsString;
    }

}