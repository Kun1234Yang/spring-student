package com.student.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    @Around("execution(* com.student.service..*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("🚀 AOP 前置逻辑: " + joinPoint.getSignature());
        Object result = joinPoint.proceed();
        System.out.println("✅ AOP 后置逻辑: " + joinPoint.getSignature());
        return result;
    }
}
