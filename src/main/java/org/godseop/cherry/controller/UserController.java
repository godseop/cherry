package org.godseop.cherry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.core.model.Condition;
import org.godseop.cherry.core.model.Result;
import org.godseop.cherry.dto.User;
import org.godseop.cherry.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="user/")
public class UserController {

    private final UserService userService;

    @PostMapping("retrieveUserList")
    public ResponseEntity<Result> retrieveUserList(@RequestBody Condition condition) {
        Result result = new Result();

        List<User> userList = userService.selectUserList();

        result.put("userList", userList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
