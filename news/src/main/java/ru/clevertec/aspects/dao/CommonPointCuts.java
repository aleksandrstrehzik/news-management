package ru.clevertec.aspects.dao;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointCuts {

    @Pointcut("execution(public * findById(..))")
    public void isWithFindByIdMethod() {
    }

    @Pointcut("execution(public * save(..))")
    public void isWithSaveMethod() {
    }

    @Pointcut("execution(public void deleteById(..))")
    public void isWithDeleteMethod() {
    }
}
