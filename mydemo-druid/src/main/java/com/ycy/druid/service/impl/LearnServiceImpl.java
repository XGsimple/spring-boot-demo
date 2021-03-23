package com.ycy.druid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycy.druid.mapper.LearnResourceMapper;
import com.ycy.druid.domain.LearnResource;
import com.ycy.druid.domain.User;
import com.ycy.druid.model.LeanQueryLeanListReq;
import com.ycy.druid.service.LearnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by tengj on 2017/4/7.
 */
@Service
public class LearnServiceImpl extends ServiceImpl<LearnResourceMapper, LearnResource> implements LearnService {

    @Autowired
    private LearnResourceMapper learnResourceMapper;

    @Autowired
    private LearnService learnService;



    @Override
    public void deleteBatch(Long[] ids) {
        learnService.removeByIds(Arrays.asList(ids));
        LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        lambdaQueryWrapper.in(User::getId, ids);
        learnResourceMapper.deleteBatchIds(Arrays.asList(ids));
        learnService.removeByIds(Arrays.asList(ids));
    }

    @Override
    public Page<LearnResource> queryLearnResoucePage(Page<LeanQueryLeanListReq> reqPage) {
        LambdaQueryWrapper<LearnResource> lambdaQueryWrapper = Wrappers.<LearnResource>lambdaQuery();
        lambdaQueryWrapper.gt(LearnResource::getId, 0L);
        Page<LearnResource> resourceQueryPage = new Page<>(reqPage.getCurrent(), reqPage.getSize());
        Page<LearnResource> resultPage = learnService.page(resourceQueryPage, lambdaQueryWrapper);
        return resultPage;
    }


}
