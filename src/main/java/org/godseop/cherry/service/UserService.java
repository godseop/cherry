package org.godseop.cherry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.core.exception.CherryException;
import org.godseop.cherry.core.model.Condition;
import org.godseop.cherry.core.model.Error;
import org.godseop.cherry.dao.UserDao;
import org.godseop.cherry.dto.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    public List<User> selectUserList() {
        return userDao.selectUserList();
    }

    public User selectUser(Condition condition) {
        return userDao.selectUser(condition);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userDao.selectUserByUserId(userId)
                .orElseThrow(() -> new CherryException(Error.USER_NOT_FOUND));
    }
}
