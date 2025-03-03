package com.example.demo.aspect;

import com.example.demo.annotation.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class RateLimitAspect {
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastResetTimes = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint point, RateLimit rateLimit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String key = ip + ":" + point.getSignature().getName();

        long currentTime = System.currentTimeMillis();
        AtomicInteger count = requestCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
        Long lastResetTime = lastResetTimes.get(key);

        if (lastResetTime == null || currentTime - lastResetTime > rateLimit.seconds() * 1000) {
            count.set(0);
            lastResetTimes.put(key, currentTime);
        }

        int currentCount = count.incrementAndGet();
        if (currentCount > rateLimit.limit()) {
            throw new RuntimeException("Превышен лимит запросов. Попробуйте позже.");
        }

        return point.proceed();
    }
} 