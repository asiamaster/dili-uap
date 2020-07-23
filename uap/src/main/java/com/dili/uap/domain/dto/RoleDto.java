package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.sdk.domain.Role;

import javax.persistence.Column;
import java.util.List;

/**
 * 角色查询对象
 */
public interface RoleDto extends Role {

    @Operator(Operator.IN)
    @Column(name = "`id`")
    List<String> getIds();
    void setIds(List<String> ids);


}
