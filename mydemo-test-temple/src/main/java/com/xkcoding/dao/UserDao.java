package com.xkcoding.dao;

import com.xkcoding.component.User;
import org.springframework.stereotype.Repository;

/**
 * @author xugangq
 * @date 2021/6/8 20:21
 */
@Repository
public interface UserDao {
    User getUserById(Integer id);

    Integer insertUser(User user);

    void updateUser(User user);

    void deleteBatch(Integer[] ids);
}
