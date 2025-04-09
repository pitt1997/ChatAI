package com.lijs.chatai.user.model.pager;

import com.lijs.chatai.common.web.pager.BasePage;
import com.lijs.chatai.core.model.vo.UserVO;
import lombok.Data;

import java.util.List;

/**
 * @author ljs
 * @date 2025-01-14
 * @description
 */
@Data
public class UserPage extends BasePage<UserVO> {

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
