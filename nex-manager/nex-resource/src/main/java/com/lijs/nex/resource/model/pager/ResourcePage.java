package com.lijs.nex.resource.model.pager;

import com.lijs.nex.common.web.pager.BasePage;
import com.lijs.nex.core.model.vo.ResourceVO;
import lombok.Data;

import java.util.List;

/**
 * @author ljs
 * @date 2025-01-14
 * @description
 */
@Data
public class ResourcePage extends BasePage<ResourceVO> {

    private String name;

    private String resourceGroupId;

    private List<Integer> status;

}
