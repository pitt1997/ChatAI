package com.lijs.nex.role.model.pager;

import com.lijs.nex.common.web.pager.BasePage;
import com.lijs.nex.core.model.vo.UserVO;
import lombok.Data;

import java.util.List;

/**
 * @author ljs
 * @date 2025-01-14
 * @description
 */
@Data
public class RolePage extends BasePage<UserVO> {

    private String name;

    private String cnName;

    private String nickname;

    private String organizationId;

    private String mobile;

    private String email;

    private Integer gender;

    private List<Integer> status;

    private String groupId;

}
