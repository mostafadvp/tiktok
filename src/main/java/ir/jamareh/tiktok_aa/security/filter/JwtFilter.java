package ir.jamareh.tiktok_aa.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jamareh.tiktok_aa.TiktokResponse;
import ir.jamareh.tiktok_aa.security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * used to check token is valid or not
 */
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                username = jwtService.extractUserName(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new IllegalArgumentException("Invalid or expired token");
                }
            }
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException | UsernameNotFoundException e) {
            log.error("Invalid or expired token:{}", e.getMessage());
            handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, new TiktokResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Internal server error:{}", e.getMessage());
            handleErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new TiktokResponse<>(false, e.getMessage(), null));
        }
    }

    private void handleErrorResponse(HttpServletResponse response, int status, TiktokResponse<?> apiResponse) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
