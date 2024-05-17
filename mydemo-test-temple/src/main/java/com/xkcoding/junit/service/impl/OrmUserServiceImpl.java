package com.xkcoding.junit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkcoding.junit.dao.OrmUserDao;
import com.xkcoding.junit.entity.OrmUser;
import com.xkcoding.junit.service.OrmUserService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Spring Boot Demo Orm 系列示例表(OrmUser)表服务实现类
 *
 * @author makejava
 * @since 2024-05-17 17:11:47
 */
@Service("ormUserService")
public class OrmUserServiceImpl extends ServiceImpl<OrmUserDao, OrmUser> implements OrmUserService {

    @Override
    public Boolean saveException(OrmUser user) throws SQLException {
        save(user);
        throw new SQLException("SQL is not valid");
    }
}

