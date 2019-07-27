package org.godseop.cherry.core.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.core.exception.CherryException;
import org.godseop.cherry.core.model.Error;
import org.godseop.cherry.dto.User;
import org.godseop.cherry.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String userId = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = userService.authenticate(userId, password);
        if (user == null)
            throw new CherryException(Error.LOGIN_FAIL);
        user.setPassword(null);

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}