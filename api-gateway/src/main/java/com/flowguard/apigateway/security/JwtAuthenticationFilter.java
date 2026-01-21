package com.flowguard.apigateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String ROLES_HEADER = "X-Roles";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .map(authentication -> applyIdentityHeaders(exchange.getRequest(), authentication))
                .defaultIfEmpty(exchange.getRequest())
                .flatMap(request -> chain.filter(exchange.mutate().request(request).build()));
    }

    private ServerHttpRequest applyIdentityHeaders(
            ServerHttpRequest request,
            JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        String userId = jwt.getSubject();
        String roles = resolveRoles(jwt, authentication.getAuthorities());

        ServerHttpRequest.Builder builder = request.mutate();
        if (userId != null && !userId.isBlank()) {
            builder.header(USER_ID_HEADER, userId);
        }
        if (roles != null) {
            builder.header(ROLES_HEADER, roles);
        }
        return builder.build();
    }

    private String resolveRoles(Jwt jwt, Collection<GrantedAuthority> authorities) {
        Object rolesClaim = jwt.getClaim("roles");
        if (rolesClaim instanceof Collection<?> rolesCollection) {
            return rolesCollection.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }
        if (rolesClaim instanceof String rolesString) {
            return rolesString;
        }
        if (authorities == null || authorities.isEmpty()) {
            return "";
        }
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return String.join(",", roles);
    }
}
