package org.godseop.cherry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.core.jwt.JwtTokenProvider;
import org.godseop.cherry.core.model.Result;
import org.godseop.cherry.dto.User;
import org.godseop.cherry.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<Result> signin(@RequestBody User user) {
        Result result = new Result();

        String userId = user.getUserId();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, user.getPassword()));
        String token = jwtTokenProvider.createToken(userId, userService.loadUserByUsername(userId).getAuthCode());

        result.put("token", token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Result> signup(@RequestBody User user) {
        Result result = new Result();

        userService.insertUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
