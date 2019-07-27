package org.godseop.cherry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.core.exception.CherryException;
import org.godseop.cherry.core.model.Condition;
import org.godseop.cherry.core.model.Error;
import org.godseop.cherry.dao.UserDao;
import org.godseop.cherry.dto.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    public List<User> selectUserList(Condition condition) {
        return userDao.selectUserList(condition);
    }

    public int selectUserListCount(Condition condition) {
        return userDao.selectUserListCount(condition);
    }

    public User selectUser(Condition condition) {
        return userDao.selectUser(condition);
    }

    @Transactional
    public void insertUser(User user) {
        if (userDao.selectUserByUserId(user.getUserId()) != null) {
            throw new CherryException(Error.DUPLICATED_USER_ID);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        userDao.insertUser(user);
    }

    @Override
    public User loadUserByUsername(String userId) {
        return userDao.selectUserByUserId(userId);
    }

    @Transactional(readOnly = true)
    public User authenticate(String userId, String password) {
        User user = loadUserByUsername(userId);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (user == null) {
            throw new CherryException(Error.USER_NOT_FOUND);
        } else if (encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
}
