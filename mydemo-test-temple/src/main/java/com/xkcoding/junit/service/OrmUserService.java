package com.xkcoding.junit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xkcoding.junit.entity.OrmUser;

import java.sql.SQLException;

/**
 * Spring Boot Demo Orm 系列示例表(OrmUser)表服务接口
 *
 * @author makejava
 * @since 2024-05-17 17:11:47
 */
public interface OrmUserService extends IService<OrmUser> {

    Boolean saveException(OrmUser user) throws SQLException;

}

