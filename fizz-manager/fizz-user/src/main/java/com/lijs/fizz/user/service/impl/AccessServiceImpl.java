package com.lijs.fizz.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.fizz.core.model.entity.AccessDO;
import com.lijs.fizz.user.dao.AccessDAO;
import com.lijs.fizz.user.service.AccessService;
import org.springframework.stereotype.Service;

/**
 * @author pitt1
 * @description 针对表【t_access(访问控制表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class AccessServiceImpl extends ServiceImpl<AccessDAO, AccessDO>
        implements AccessService {

}
