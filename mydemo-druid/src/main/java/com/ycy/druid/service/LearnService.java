package com.ycy.druid.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ycy.druid.domain.LearnResource;
import com.ycy.druid.model.LeanQueryLeanListReq;

/**
 * Created by tengj on 2017/4/7.
 */

public interface LearnService extends IService<LearnResource> {
    Page<LearnResource> queryLearnResoucePage(Page<LeanQueryLeanListReq> reqPage);

    void deleteBatch(Long[] ids);
}
