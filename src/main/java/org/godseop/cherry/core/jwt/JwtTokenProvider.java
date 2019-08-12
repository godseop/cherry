package org.godseop.cherry.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.*;
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
import javax.servlet.http.HttpServletRequest;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserService userService;

    private ECPrivateKey privateKey;

    private ECPublicKey publicKey;

    @PostConstruct
    protected void init() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ES");
        keyPairGenerator.initialize(new ECGenParameterSpec("secp521r1"), new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        privateKey = (ECPrivateKey) keyPair.getPrivate();
        publicKey = (ECPublicKey) keyPair.getPublic();

        log.error(" Private Key = {}", Base64.getEncoder().encodeToString(privateKey.getS().toByteArray()));
        log.error("Public X Key = {}", Base64.getEncoder().encodeToString(publicKey.getW().getAffineX().toByteArray()));
        log.error("Public Y Key = {}", Base64.getEncoder().encodeToString(publicKey.getW().getAffineY().toByteArray()));
    }

    public String createToken(String userId, String authCode, String companyCode) {
        Algorithm algorithm = Algorithm.ECDSA256(publicKey, privateKey);

        Calendar calendar = new GregorianCalendar();
        Date now = calendar.getTime();
        Date validity = new Date(now.getTime() + 60000);  // 1 minutes valid

        return JWT.create()
                .withArrayClaim(userId, new String[] {authCode, companyCode})
                .withIssuedAt(now)
                .withIssuer("Cherry")
                .withAudience("http://cherry.godseop.org")
                .withExpiresAt(validity)
                .sign(algorithm);
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
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.ECDSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Cherry")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            return true;
        } catch (JWTVerificationException exception) {
            throw new CherryException(Error.TOKEN_VERIFY_EXCEPTION);
        } catch (ExpiredJwtException exception) {
            throw new CherryException(Error.TOKEN_EXPIRED);
        } catch (SignatureException exception) {
            throw new CherryException(Error.TOKEN_INVALID);
        } catch (MalformedJwtException exception) {
            throw new CherryException(Error.TOKEN_MALFORMED);
        }
    }
}
