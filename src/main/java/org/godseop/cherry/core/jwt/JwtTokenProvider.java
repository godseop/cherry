package org.godseop.cherry.core.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.core.exception.CherryException;
import org.godseop.cherry.core.model.Error;
import org.godseop.cherry.dto.User;
import org.godseop.cherry.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserService userService;

    private ECPrivateKey privateKey;

    private ECPublicKey publicKey;

    @PostConstruct
    protected void init() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec("secp521r1"), new SecureRandom());
        KeyPair keyPair = kpg.generateKeyPair();
        privateKey = (ECPrivateKey) keyPair.getPrivate();
        publicKey = (ECPublicKey) keyPair.getPublic();

        log.error(" Private Key = {}", Base64.getEncoder().encodeToString(privateKey.getS().toByteArray()));
        log.error("Public X Key = {}", Base64.getEncoder().encodeToString(publicKey.getW().getAffineX().toByteArray()));
        log.error("Public Y Key = {}", Base64.getEncoder().encodeToString(publicKey.getW().getAffineY().toByteArray()));
    }

    public String createToken(String userId, String authCode, String companyCode) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("authCode", authCode);
        claims.put("companyCode", companyCode);

        Calendar calendar = new GregorianCalendar();
        Date now = calendar.getTime();
        Date validity = new Date(now.getTime() + 60000);  // 1 minutes valid

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)          // 인증정보
                .setIssuedAt(now)           // 토큰발급시간
                .setIssuer("Cherry")        // 토큰발급자
                .setAudience("http://cherry.godseop.org")
                .setExpiration(validity)    // 토큰만료시간
                .signWith(SignatureAlgorithm.ES256, privateKey);

        return builder.compact();
    }

    public Authentication getAuthentication(String token) {
        User user = userService.loadUserByUsername(getUserId(token));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody().getSubject();
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
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException exception) {
            throw new CherryException(Error.TOKEN_EXPIRED);
        } catch (SignatureException exception) {
            throw new CherryException(Error.TOKEN_INVALID);
        } catch (MalformedJwtException exception) {
            throw new CherryException(Error.TOKEN_MALFORMED);
        }
    }
}
