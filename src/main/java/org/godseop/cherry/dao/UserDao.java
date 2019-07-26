package org.godseop.cherry.dao;

import org.apache.ibatis.annotations.Mapper;
import org.godseop.cherry.core.model.Condition;
import org.godseop.cherry.dto.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserDao {
    List<User> selectUserList();

    User selectUser(Condition condition);

    Optional<User> selectUserByUserId(String userId);
}
