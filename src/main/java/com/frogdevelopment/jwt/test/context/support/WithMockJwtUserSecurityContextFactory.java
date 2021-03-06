package com.frogdevelopment.jwt.test.context.support;

import static com.frogdevelopment.jwt.JwtAuthenticationToken.AUTHORITIES_KEY;

import com.frogdevelopment.jwt.JwtAuthenticationToken;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.ArrayList;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.StringUtils;

public final class WithMockJwtUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {

    @Override
    public SecurityContext createSecurityContext(final WithMockJwtUser withUser) {
        if (withUser.username() == null || withUser.username().strip().length() == 0) {
            throw new IllegalArgumentException("Username required");
        }

        final var claims = new DefaultClaims();
        claims.setSubject(withUser.username());

        final var authorities = new ArrayList<String>();
        for (String role : withUser.roles()) {
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }
            authorities.add(role);
        }
        claims.put(AUTHORITIES_KEY, authorities);

        for (final var claim : withUser.claims()) {
            claims.put(claim.name(), claim.value());
        }

        final var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new JwtAuthenticationToken(claims, withUser.token()));
        return context;
    }
}
