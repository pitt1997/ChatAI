package com.lijs.chatai.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.core.model.entity.resource.ResourceDO;
import com.lijs.chatai.resource.dao.ResourceDAO;
import com.lijs.chatai.resource.service.ResourceService;
import org.springframework.stereotype.Service;

/**
 * @author pitt1
 * @description 针对表【t_resource(资源表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceDAO, ResourceDO>
        implements ResourceService {

}
