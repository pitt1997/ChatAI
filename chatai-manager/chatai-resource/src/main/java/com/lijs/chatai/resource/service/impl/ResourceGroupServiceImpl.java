package com.lijs.chatai.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.core.model.entity.resource.ResourceGroupDO;
import com.lijs.chatai.resource.dao.ResourceGroupDAO;
import com.lijs.chatai.resource.service.ResourceGroupService;
import org.springframework.stereotype.Service;

/**
 * @author pitt1
 * @description 针对表【t_resource_group(资源组表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class ResourceGroupServiceImpl extends ServiceImpl<ResourceGroupDAO, ResourceGroupDO>
        implements ResourceGroupService {

}
