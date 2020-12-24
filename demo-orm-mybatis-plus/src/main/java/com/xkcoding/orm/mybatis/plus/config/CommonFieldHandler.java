package com.xkcoding.orm.mybatis.plus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 通用字段填充
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-08 17:40
 */
@Slf4j
@Component
public class CommonFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("数据库表创建，开始填充系统字段...");
//        //过期方法
//        this.setFieldValByName("createTime", new Date(), metaObject);
//        this.setFieldValByName("lastUpdateTime", new Date(), metaObject);

        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "lastUpdateTime", Date.class, new Date());
        //this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        //this.strictInsertFill(metaObject, "lastUpdateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "version", Integer.class, 1);
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("数据库表更新，开始更新系统字段...");
//        //过期方法
//        this.setFieldValByName("lastUpdateTime", new Date(), metaObject);
        this.strictUpdateFill(metaObject, "lastUpdateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
