-- SQL init Table init
CREATE TABLE `t_organization`
(
    `id`          varchar(32)  NOT NULL COMMENT '主键',
    `code`        varchar(32)  DEFAULT NULL COMMENT '编码',
    `name`        varchar(512) NOT NULL COMMENT '名称',
    `parent_id`   varchar(32)  DEFAULT NULL COMMENT '父节点id',
    `path`        varchar(512) DEFAULT NULL COMMENT '全路径',
    `description` varchar(512) DEFAULT NULL COMMENT '描述信息',
    `status`      tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`   tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32)  DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32)  DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构表';

CREATE TABLE `t_user`
(
    `id`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '主键',
    `name`            varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
    `nickname`        varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
    `cn_name`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中文名称',
    `icon`            blob NULL COMMENT '头像',
    `password`        varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
    `organization_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织机构ID',
    `mobile`          varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
    `email`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
    `gender`          tinyint(1) NULL DEFAULT NULL COMMENT '性别',
    `description`     varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `status`          tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`       tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user`     varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user`     varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time`     timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`     timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE `t_resource_group`
(
    `id`          varchar(32) NOT NULL COMMENT '主键',
    `name`        varchar(32)   DEFAULT NULL COMMENT '资源组名称',
    `parent_id`   varchar(32)   DEFAULT NULL COMMENT '资源组父节点ID',
    `path`        varchar(1024) DEFAULT NULL COMMENT '资源组路径',
    `description` varchar(4000) DEFAULT NULL COMMENT '资源组描述',
    `status`      tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`   tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32)   DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32)   DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源组表';

CREATE TABLE `t_resource`
(
    `id`                varchar(32)   NOT NULL COMMENT '资源ID',
    `name`              varchar(32)   NOT NULL COMMENT '资源名称',
    `icon`              blob COMMENT '资源图片',
    `url`               varchar(1024) NOT NULL COMMENT '资源地址',
    `type`              varchar(32)   NOT NULL COMMENT '资源类型',
    `login_url`         varchar(1024) NOT NULL COMMENT '资源登录地址',
    `logout_url`        varchar(1024) DEFAULT NULL COMMENT '资源登出地址',
    `logout_type`       tinyint(1) DEFAULT NULL COMMENT '单点退出方式，0-不处理，1-后台',
    `protocol`          varchar(255)  DEFAULT NULL COMMENT '单点登录协议，OIDC、SAML、OAUTH、CAS',
    `protocol_content`  text COMMENT '协议配置内容',
    `resource_group_id` varchar(32)   NOT NULL COMMENT '资源分组ID',
    `content`           text COMMENT '扩展字段',
    `description`       varchar(4000) DEFAULT NULL COMMENT '资源描述',
    `status`            tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`         tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user`       varchar(32)   DEFAULT NULL COMMENT '创建人id',
    `update_user`       varchar(32)   DEFAULT NULL COMMENT '修改人id',
    `create_time`       timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`       timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源表';

CREATE TABLE `t_user_group`
(
    `id`          varchar(32)  NOT NULL COMMENT '主键',
    `name`        varchar(500) NOT NULL COMMENT '用户组名称',
    `parent_id`   varchar(32)   DEFAULT NULL COMMENT '用户组父节点ID',
    `path`        varchar(1024) DEFAULT NULL COMMENT '用户组路径',
    `status`      tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`   tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32)   DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32)   DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组表';

CREATE TABLE `t_permission`
(
    `id`          varchar(32)  NOT NULL COMMENT '主键',
    `name`        varchar(255) NOT NULL COMMENT '权限点名称',
    `route`       varchar(255) NOT NULL COMMENT '权限点URL路由',
    `action`      varchar(32)  NOT NULL COMMENT '权限点动作新增、删除、查看',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能权限表';

CREATE TABLE `t_role`
(
    `id`          varchar(32) NOT NULL COMMENT '主键',
    `parent_id`   varchar(32)   DEFAULT NULL COMMENT '上级角色ID',
    `name`        varchar(32) NOT NULL COMMENT '角色名称',
    `path`        varchar(1024) DEFAULT NULL COMMENT '角色全路径',
    `status`      tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`   tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32)   DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32)   DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色表';

CREATE TABLE `t_role_user`
(
    `id`          varchar(32) NOT NULL COMMENT '主键',
    `role_id`     varchar(32) NOT NULL COMMENT '角色ID',
    `user_id`     varchar(32) NOT NULL COMMENT '用户ID',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色和用户关联表';

CREATE TABLE `t_role_permission`
(
    `id`            varchar(32) NOT NULL COMMENT '主键',
    `role_id`       varchar(32) NOT NULL COMMENT '角色ID',
    `permission_id` varchar(32) NOT NULL COMMENT '权限点ID',
    `tenant_id`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user`   varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user`   varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time`   timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`   timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色关联权限表';

CREATE TABLE `t_role_resource_group`
(
    `id`                varchar(32) NOT NULL COMMENT '主键',
    `role_id`           varchar(32) NOT NULL COMMENT '角色ID',
    `resource_group_id` varchar(32) NOT NULL COMMENT '资源组ID',
    `tenant_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user`       varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user`       varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time`       timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`       timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色关联资源组表';

CREATE TABLE `t_role_organization`
(
    `id`              varchar(32) NOT NULL COMMENT '主键',
    `role_id`         varchar(32) NOT NULL COMMENT '角色ID',
    `organization_id` varchar(32) NOT NULL COMMENT '组织机构ID',
    `tenant_id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user`     varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user`     varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time`     timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`     timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色关联组织机构表';

CREATE TABLE `t_access`
(
    `id`            varchar(32) NOT NULL COMMENT '主键',
    `resource_id`   varchar(32) NOT NULL COMMENT '资源ID',
    `user_group_id` varchar(32) NOT NULL COMMENT '用户组ID',
    `tenant_id`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user`   varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user`   varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time`   timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`   timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`ID`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='访问控制表';

CREATE TABLE `t_resource_account`
(
    `id`          VARCHAR(32) NOT NULL COMMENT '主键ID',
    `name`        VARCHAR(255) COMMENT '账号名称',
    `resource_id` VARCHAR(32) NOT NULL COMMENT '资源ID',
    `status`      tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`   tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源账号表';

CREATE TABLE `t_user_account`
(
    `id`          VARCHAR(32) NOT NULL COMMENT '主键ID',
    `user_id`     VARCHAR(32) NOT NULL COMMENT '用户ID',
    `account_id`  VARCHAR(32) NOT NULL COMMENT '资源账号ID',
    `tenant_id`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user` varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user` varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账号表';

CREATE TABLE `t_user_register`
(
    `id`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '主键',
    `name`            varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
    `nickname`        varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
    `cn_name`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中文名称',
    `icon`            blob NULL COMMENT '头像',
    `organization_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织机构ID',
    `mobile`          varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
    `email`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
    `gender`          tinyint(1) NULL DEFAULT NULL COMMENT '性别',
    `description`     varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `status`          tinyint(1) DEFAULT NULL COMMENT '状态',
    `is_delete`       tinyint(1) DEFAULT NULL COMMENT '是否删除 0-逻辑未删除 1-逻辑已删除',
    `tenant_id`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户id',
    `create_user`     varchar(32) DEFAULT NULL COMMENT '创建人id',
    `update_user`     varchar(32) DEFAULT NULL COMMENT '修改人id',
    `create_time`     timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`     timestamp NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户注册表';

-- SQL init data

-- 初始化根节点
INSERT INTO `nex`.`t_organization`(`id`, `code`, `name`, `parent_id`, `path`, `description`, `status`, `create_user`, `update_user`, `create_time`, `update_time`) VALUES ('1', NULL, '根组织', NULL, '/', '根组织节点', 1, '1', NULL, '2024-12-20 10:14:06', NULL);

-- 初始化管理员用户
INSERT INTO `nex`.`t_user`(`id`, `name`, `nickname`, `cn_name`, `icon`, `password`, `organization_id`, `mobile`, `email`, `gender`, `description`, `status`, `is_delete`, `tenant_id`, `create_user`, `update_user`, `create_time`, `update_time`) VALUES ('1', 'admin', NULL, '超级管理员', NULL, '123456', '1', NULL, NULL, NULL, NULL, 0, 0, NULL, '1', NULL, '2024-12-20 11:32:31', NULL);

-- 初始化用户组
INSERT INTO `nex`.`t_user_group`(`id`, `name`, `parent_id`, `path`, `status`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1', '用户组', '0', '/', 1, '1', '2024-12-20 13:51:40', NULL, NULL);

-- 初始化资源组
INSERT INTO `nex`.`t_resource_group`(`id`, `name`, `parent_id`, `path`, `status`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1', '资源组', '0', '/', 1, '1', '2024-12-20 13:50:53', NULL, NULL);

-- 初始化角色表
INSERT INTO `nex`.`t_role`(`id`, `parent_id`, `name`, `path`, `status`, `tenant_id`, `create_user`, `create_time`, `update_user`, `update_time`) VALUES ('1', NULL, '超级管理员', '/1', 1, NULL, '1', '2024-12-20 14:10:26', NULL, NULL);

INSERT INTO `nex`.`t_role_user`(`id`, `role_id`, `user_id`) VALUES ('1', '1', '1');
