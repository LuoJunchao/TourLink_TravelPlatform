package org.tourlink.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * 日志切面
 * 用于记录方法执行时间和参数
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    
    /**
     * 定义切点：所有service包下的方法
     */
    @Pointcut("execution(* org.tourlink.*.service.*.*(..))")
    public void servicePointcut() {
    }
    
    /**
     * 定义切点：所有controller包下的方法
     */
    @Pointcut("execution(* org.tourlink.*.controller.*.*(..))")
    public void controllerPointcut() {
    }
    
    /**
     * 环绕通知：记录服务方法执行时间
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("servicePointcut()")
    public Object logServiceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Service");
    }
    
    /**
     * 环绕通知：记录控制器方法执行时间
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("controllerPointcut()")
    public Object logControllerMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Controller");
    }
    
    /**
     * 记录方法执行时间
     * @param joinPoint 连接点
     * @param type 方法类型
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    private Object logMethodExecution(ProceedingJoinPoint joinPoint, String type) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        log.info("{} - 开始执行 {}.{}", type, className, methodName);
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            log.info("{} - 执行完成 {}.{}, 耗时: {}ms", type, className, methodName, stopWatch.getTotalTimeMillis());
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            log.error("{} - 执行异常 {}.{}: {}, 耗时: {}ms", type, className, methodName, e.getMessage(), stopWatch.getTotalTimeMillis(), e);
            throw e;
        }
    }
}
