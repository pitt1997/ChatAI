package com.lijs.chatai.core.model.entity.resource;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.chatai.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 资源表
 *
 * @TableName t_resource
 */
@Data
@TableName(value = "t_resource")
public class ResourceDO extends BaseEntity implements Serializable {

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源地址
     */
    private String url;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 资源登录地址
     */
    private String loginUrl;

    /**
     * 资源登出地址
     */
    private String logoutUrl;

    /**
     * 单点退出方式，0-不处理，1-后台
     */
    private Integer logoutType;

    /**
     * 单点登录协议，OIDC、SAML、OAUTH、CAS
     */
    private String protocol;

    /**
     * 协议配置内容
     */
    private String protocolContent;

    /**
     * 资源分组ID
     */
    private String resourceGroupId;

    /**
     * 扩展字段
     */
    private String content;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否删除 0-逻辑未删除 1-逻辑已删除
     */
    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;

    /**
     * 资源图片
     */
    private byte[] icon;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}