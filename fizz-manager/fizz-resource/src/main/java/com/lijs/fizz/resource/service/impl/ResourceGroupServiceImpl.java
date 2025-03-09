package com.lijs.fizz.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.fizz.core.model.entity.resource.ResourceGroupDO;
import com.lijs.fizz.resource.dao.ResourceGroupDAO;
import com.lijs.fizz.resource.service.ResourceGroupService;
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
