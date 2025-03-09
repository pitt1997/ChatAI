package com.lijs.nex.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 主键ID
 *
 * @author ljs
 * @date 2024-12-19
 * @description
 */
public abstract class BaseEntityID {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
