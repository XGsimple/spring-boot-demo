package com.ycy.druid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycy.druid.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User mySelectAll();
}
