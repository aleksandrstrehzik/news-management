package ru.clevertec.logging.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class CommonPointcuts {

    @Pointcut("@annotation(ru.clevertec.logging.annotations.AroundLog)")
    public void aroundLogging() {
    }

    @Pointcut("@annotation(ru.clevertec.logging.annotations.BeforeLog)")
    public void beforeLogging() {
    }

    @Pointcut("@annotation(ru.clevertec.logging.annotations.AfterLog)")
    public void afterLogging() {
    }

    @Pointcut("@annotation(ru.clevertec.logging.annotations.AfterReturningLog)")
    public void afterReturningLogging() {
    }
    @Pointcut("@annotation(ru.clevertec.logging.annotations.AfterThrowingLog)")
    public void afterThrowingLogging() {
    }

    @Around("aroundLogging()")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String invokeClass = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Class, method {}.{} with argument: {}", invokeClass, method, args);
        Object result = joinPoint.proceed();

        log.info("Returning result: {}", result);
        return result;
    }

    @Before("beforeLogging()")
    public void beforeLog(JoinPoint joinPoint) {
        String invokeClass = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Class, method {}.{} with argument: {}", invokeClass, method, args);
    }

    @After("afterLogging()")
    public void afterLog(JoinPoint joinPoint) {
        String invokeClass = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("After Executing {}.{} with argument: {}", invokeClass, method, args);
    }

    @AfterReturning(value = "afterReturningLogging()", returning = "result")
    public void afterReturningLog(JoinPoint joinPoint, Object result) {
        String invokeClass = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("After Executing {}.{} with argument: {}, returning value: {}", invokeClass, method, args, result);
    }

    @AfterThrowing(value = "afterThrowingLogging()", throwing = "exception")
    public void afterThrowingLog(JoinPoint joinPoint, Throwable exception) {
        String invokeClass = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("After Executing {}.{} with argument: {}, returning exception: ", invokeClass, method, args, exception);
    }
}
