package org.godseop.cherry.dao;

import org.apache.ibatis.annotations.Mapper;
import org.godseop.cherry.dto.User;

import java.util.List;

@Mapper
public interface UserDao {
    List<User> selectUserList();
}
