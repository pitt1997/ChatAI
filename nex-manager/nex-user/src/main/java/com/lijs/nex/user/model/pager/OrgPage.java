package com.lijs.nex.user.model.pager;

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
public class OrgPage extends BasePage<UserVO> {

    private String name;

    private List<Integer> status;

}
