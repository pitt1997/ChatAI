package com.lijs.chatai.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.user.service.UserRegisterService;
import com.lijs.chatai.core.model.entity.UserRegisterDO;
import com.lijs.chatai.user.dao.UserRegisterDAO;
import org.springframework.stereotype.Service;

/**
 * @author pitt1
 * @description 针对表【t_user_register(用户注册表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class UserRegisterDOServiceImpl extends ServiceImpl<UserRegisterDAO, UserRegisterDO>
        implements UserRegisterService {

}
