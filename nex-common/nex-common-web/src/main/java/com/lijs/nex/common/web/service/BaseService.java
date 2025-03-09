package com.lijs.nex.common.web.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.nex.common.base.utils.RegexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService<S extends BaseMapper<T>, T> extends ServiceImpl<S, T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean findSpecialChar(String name) {
        return RegexUtils.CONTAINS_SPECIAL_CHAR_PATTERN.matcher(name).find();
    }

}
