package org.godseop.cherry.dao;

import org.apache.ibatis.annotations.Mapper;
import org.godseop.cherry.core.model.Condition;
import org.godseop.cherry.dto.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserDao {

    List<User> selectUserList(Condition condition);

    int selectUserListCount(Condition condition);

    User selectUser(Condition condition);

    User selectUserByUserId(String userId);

    void insertUser(User user);

}
