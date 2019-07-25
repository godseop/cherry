package org.godseop.cherry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.dao.UserDao;
import org.godseop.cherry.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public List<User> selectUserList() {
        return userDao.selectUserList();
    }
}
