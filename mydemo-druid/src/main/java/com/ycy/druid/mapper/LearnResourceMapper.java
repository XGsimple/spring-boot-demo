package com.ycy.druid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycy.druid.domain.LearnResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface LearnResourceMapper extends BaseMapper<LearnResource> {

    List<LearnResource> queryByAuthorName(@Param("author") String author);
}
