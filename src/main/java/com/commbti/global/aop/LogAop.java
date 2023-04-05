package com.commbti.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAop {

    @Pointcut("execution(* com.commbti.controller..*.*(..)) || " +
            "execution(* com.commbti.domain..*.*(..))")
    private void pointcut() {}

    @Before("pointcut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        Method methodInfo = getMethod(joinPoint);
        log.trace("{}: 호출", methodInfo);

        Object[] args = joinPoint.getArgs();
        if (args.length < 1) {
            log.debug("no parameter");
        }
        for (Object arg : args) {
            log.debug("parameter - type = {} / value = {}", arg.getClass().getSimpleName(), arg);
        }
    }

    @AfterReturning(value = "pointcut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        Method methodInfo = getMethod(joinPoint);

//        log.debug("return - type = {} / value = {}", returnObj.getClass().getSimpleName(), returnObj);
        log.trace("{}: 종료", methodInfo);
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

}
