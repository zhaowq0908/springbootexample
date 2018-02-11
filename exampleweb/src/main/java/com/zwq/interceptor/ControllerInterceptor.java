package com.zwq.interceptor;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author: zhaowq
 * @description: web aop
 * @create: 2018-02-11 11:09
 **/
@Component
@Aspect
public class ControllerInterceptor {
    private Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    /**
     * @Description 指定切入点
     */
    @Pointcut("execution(* com.it.web..*.*(..))")
    public void anyMethod() {
        // 切点使用与任何方法
    }

    /**
     * @param jp
     * @throws Exception
     * @Description 前置通知 在切点方法集合执行前，执行前置通知
     */
    @Before(value = "anyMethod()")
    public void doBefore(JoinPoint jp) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        logger.debug("requesturl : " + request.getRequestURL().toString());
        logger.debug("classmethod : " + jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName());
        logger.debug("params : " + Arrays.toString(jp.getArgs()));

    }

    /**
     * @param jp
     * @param result
     * @Description 后置通知
     */
    @AfterReturning(value = "anyMethod()", returning = "result")
    public void doAfter(JoinPoint jp, Object result) {
        logger.info("doAfter拦截器开始---->");
    }

    /**
     * @param pjp
     * @return
     * @throws Throwable
     * @Description 环绕通知（ ##环绕通知的方法中一定要有ProceedingJoinPoint 参数,与
     * Filter中的doFilter方法类似） 异常统一捕获
     */
    @SuppressWarnings("unchecked")
    @Around(value = "anyMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Controller拦截器开始---->");

        logger.debug("Controller拦截器,服务结束---->");
        return pjp.proceed();
    }

    /**
     * @param jp
     * @Description 异常通知
     */
    @AfterThrowing(value = "anyMethod()")
    public void doThrow(JoinPoint jp) {
    }
}
