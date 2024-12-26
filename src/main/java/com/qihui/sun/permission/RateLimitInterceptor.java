package com.qihui.sun.permission;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor  implements HandlerInterceptor {

    private final Map<String, SimpleRateLimiter> rateLimiters = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
            if (rateLimit != null) {
                String key = handlerMethod.getMethod().getName();
                SimpleRateLimiter rateLimiter = rateLimiters.computeIfAbsent(key, k ->
                        new SimpleRateLimiter((int) rateLimit.permitsPerSecond(), 1, TimeUnit.SECONDS)
                );
                if (!rateLimiter.tryAcquire()) {
                    response.setStatus(429); // 429 Too Many Requests
                    response.getWriter().write("Too Many Requests");
                    return false;
                }
            }
        }
        return true;
    }

}
