package com.example.aicrud.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RestLoggingFilter extends OncePerRequestFilter {

    private static final Logger REST_LOGGER = LoggerFactory.getLogger("REST_EXECUTION_LOGGER");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullPath = StringUtils.hasText(queryString) ? uri + "?" + queryString : uri;
        String method = request.getMethod();
        String clientIp = getClientIp(request);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            // Format log message for REST execution
            String logMessage = String.format(
                    "[REST-EXECUTE] METHOD=%-6s | URI=%-35s | STATUS=%-3d | TIME=%4dms | CLIENT_IP=%s",
                    method, fullPath, status, duration, clientIp
            );

            if (status >= 500) {
                REST_LOGGER.error(logMessage);
            } else if (status >= 400) {
                REST_LOGGER.warn(logMessage);
            } else {
                REST_LOGGER.info(logMessage);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip static asset files (css, js, images, favicon) to keep REST logs focused on API calls
        return path.endsWith(".css") ||
               path.endsWith(".js") ||
               path.endsWith(".ico") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg") ||
               path.endsWith(".svg") ||
               path.endsWith(".woff2");
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }
}
