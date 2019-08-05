package org.godseop.cherry.core.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.RequiredArgsConstructor;
import org.godseop.cherry.core.exception.CherryException;
import org.godseop.cherry.core.exception.InvalidJwtAuthenticationException;
import org.godseop.cherry.core.model.Error;
import org.godseop.cherry.dto.User;
import org.godseop.cherry.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserService userService;

    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public String createToken(String userId, String authCode) {

        SecretKey signingKey = MacProvider.generateKey();

        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", authCode);
        //claims.put("company", companyCode);

        Calendar calendar = new GregorianCalendar();
        Date now = calendar.getTime();
        Date validity = new Date(now.getTime() + jwtProperties.getValidityInMs());

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)          // 인증정보
                .setIssuedAt(now)           // 이슈
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.ES256, secretKey);

        return builder.compact();
    }

    public Authentication getAuthentication(String token) {
        User user = userService.loadUserByUsername(getUserId(token));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception) {
            throw new CherryException(Error.TOKEN_EXPIRED);
        } catch (SignatureException exception) {
            throw new CherryException(Error.TOKEN_INVALID);
        } catch (MalformedJwtException exception) {
            throw new CherryException(Error.TOKEN_INVALID);
        }
    }
}
