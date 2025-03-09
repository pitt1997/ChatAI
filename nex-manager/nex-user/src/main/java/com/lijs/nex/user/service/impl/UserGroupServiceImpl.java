package com.lijs.nex.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.nex.core.model.entity.idm.UserGroupDO;
import com.lijs.nex.user.dao.UserGroupDAO;
import com.lijs.nex.user.service.UserGroupService;
import org.springframework.stereotype.Service;

/**
 * @author pitt1
 * @description 针对表【t_user_group(用户组表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupDAO, UserGroupDO>
        implements UserGroupService {

}
