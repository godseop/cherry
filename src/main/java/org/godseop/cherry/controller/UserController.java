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

    @PostMapping("list")
    public ResponseEntity<Result> list(@RequestBody Condition condition) {
        Result result = new Result();

        List<User> userList = userService.selectUserList(condition);
        int userCount = userService.selectUserListCount(condition);

        result.put("userList", userList);
        result.put("userCount", userCount);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("detail")
    public ResponseEntity<Result> detail(@RequestBody Condition condition) {
        Result result = new Result();

        User user = userService.selectUser(condition);

        result.put("user", user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
