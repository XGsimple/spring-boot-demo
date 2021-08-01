package com.xkcoding.service;

import com.xkcoding.component.User;

/**
 * @author xugangq
 * @date 2021/6/9 11:10
 */
public interface UserService {
    User queryUserById(Integer id);

    Integer saveUser(User user);

    void createNewUser(User user) throws Exception;

    void removeBatch(Integer[] ids);

    void updateUser(User user);
}
