package com.xkcoding.service;

import com.xkcoding.component.User;
import com.xkcoding.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xugangq
 * @date 2021/6/8 20:19
 */

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Override
    public User queryUserById(Integer id) {
        return userDao.getUserById(id);
    }

    @Override
    public Integer saveUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public void createNewUser(User user) throws Exception {
        // 参数校验
        if (user == null || user.getId() == null || isEmpty(user.getName())) {
            throw new IllegalArgumentException();
        }

        // 查看是否是重复数据
        Integer id = user.getId();
        User dbUser = userDao.getUserById(id);
        if (dbUser != null) {
            throw new Exception("用户已经存在");
        }

        try {
            userDao.insertUser(dbUser);
        } catch (Exception e) {
            // 隐藏Database异常，抛出服务异常
            throw new Exception("数据库语句执行失败", e);
        }
    }

    @Override
    public void removeBatch(Integer[] ids) {
        userDao.deleteBatch(ids);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    private boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
