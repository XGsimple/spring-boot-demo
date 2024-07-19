package com.xkcoding.excel.exceltool.dict.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkcoding.excel.exceltool.dict.entity.User;
import com.xkcoding.excel.exceltool.dict.mapper.UserMapper;
import com.xkcoding.excel.exceltool.dict.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-05-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
