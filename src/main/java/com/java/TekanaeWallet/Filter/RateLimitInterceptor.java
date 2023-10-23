package com.java.TekanaeWallet.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Value("${user.rate.limit}")
    private int userRateLimit;

    @Value("${system.rate.limit}")
    private int systemRateLimit;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Maintain a map of IP addresses and their request counts
    private final ConcurrentHashMap<String, RateLimitData> ipRateLimits = new ConcurrentHashMap<>();

    // Maintain system-wide rate limit data
    private RateLimitData systemRateLimitData = new RateLimitData();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String ipAddress = getClientIP(request);

        // Check the system-wide rate limit
        if (exceedsRateLimit(systemRateLimitData, systemRateLimit)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(getRateLimitExceededResponse("System"));
            return false;
        }

        // Check the rate limit for the client IP
        if (exceedsRateLimit(ipRateLimits, ipAddress, userRateLimit)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(getRateLimitExceededResponse("User"));
            return false;
        }

        incrementRequestCount(ipRateLimits, ipAddress);
        incrementRequestCount(systemRateLimitData);
        return true;
    }

    private boolean exceedsRateLimit(RateLimitData rateLimitData, int limit) {
        return rateLimitData.isRateLimited(limit);
    }

    private boolean exceedsRateLimit(ConcurrentHashMap<String, RateLimitData> rateLimits, String ipAddress, int limit) {
        RateLimitData rateLimitData = rateLimits.computeIfAbsent(ipAddress, k -> new RateLimitData());
        return rateLimitData.isRateLimited(limit);
    }

    private void incrementRequestCount(ConcurrentHashMap<String, RateLimitData> rateLimits, String ipAddress) {
        RateLimitData rateLimitData = rateLimits.computeIfAbsent(ipAddress, k -> new RateLimitData());
        rateLimitData.increment();
    }

    private void incrementRequestCount(RateLimitData rateLimitData) {
        rateLimitData.increment();
    }

    private String getRateLimitExceededResponse(String limitType) throws IOException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "Too many " + limitType + " requests. Please try again later.");
        return objectMapper.writeValueAsString(responseBody);
    }

    private String getClientIP(HttpServletRequest request) {
        String clientIpAddress = request.getRemoteAddr();
        return clientIpAddress;
    }

    private static class RateLimitData {
        private long lastRequestTime = System.currentTimeMillis();
        private int requestCount = 0;

        public boolean isRateLimited(int limit) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastRequestTime;

            if (elapsedTime > TimeUnit.MINUTES.toMillis(1)) {
                // Reset if more than a minute has passed
                lastRequestTime = currentTime;
                requestCount = 0;
            }

            if (requestCount >= limit) {
                return true; // Rate limited
            }

            return false; // Not rate limited
        }

        public void increment() {
            requestCount++;
        }
    }
}
