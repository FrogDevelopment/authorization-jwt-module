package com.frogdevelopment.jwt;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import io.jsonwebtoken.Claims;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken implements Authentication {

    public static final String AUTHORITIES_KEY = "authorities";

    @Getter
    private final String principal;
    @Getter
    private final String name;
    @Getter
    private final Collection<GrantedAuthority> authorities;
    @Getter
    private final Map<String, Object> details;
    @Getter
    @Setter
    private boolean authenticated;
    @Getter
    private final String tokenString;

    @SuppressWarnings("unchecked")
    public JwtAuthenticationToken(final Claims claims, final String token) {
        this.tokenString = token;
        this.principal = claims.getSubject();
        this.name = claims.getSubject();
        this.authorities = ((List<String>) claims.getOrDefault(AUTHORITIES_KEY, emptyList()))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toUnmodifiableList());
        this.details = claims.entrySet()
                .stream()
                .filter(entry -> !AUTHORITIES_KEY.equals(entry.getKey()))
                .collect(toUnmodifiableMap(Entry::getKey, Entry::getValue));
        this.authenticated = true;
    }

    public boolean hasDetail(final String key) {
        return details.containsKey(key);
    }

    public Object getDetail(final String key) {
        return details.get(key);
    }

    @Override
    public String getCredentials() {
        return null;
    }

}
