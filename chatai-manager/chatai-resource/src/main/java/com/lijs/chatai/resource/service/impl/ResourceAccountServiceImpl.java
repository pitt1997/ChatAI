package com.lijs.chatai.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.core.model.entity.resource.ResourceAccountDO;
import com.lijs.chatai.resource.dao.ResourceAccountDAO;
import com.lijs.chatai.resource.service.ResourceAccountService;
import org.springframework.stereotype.Service;

/**
 * @author pitt1
 * @description 针对表【t_resource_account(资源账号表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class ResourceAccountServiceImpl extends ServiceImpl<ResourceAccountDAO, ResourceAccountDO>
        implements ResourceAccountService {

}
