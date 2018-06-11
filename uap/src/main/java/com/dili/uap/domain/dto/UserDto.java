package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.domain.User;

import javax.persistence.Column;
import java.util.List;

public interface UserDto extends User{

    String getOldPassword();
    void setOldPassword(String oldPassword);
    
    String getNewPassword();
    void setNewPassword(String newPassword);
    
    String getConfirmPassword();
    void setConfirmPassword(String confirmPassword);

    /**
     * 关键字查询
     * @return
     */
    String getKeywords();
    void setKeywords(String keywords);

    /**
     * 角色ID查询
     * @return
     */
    Long getRoleId();
    void setRoleId(Long roleId);

    @Column(name = "`user_roles`")
    @FieldDef(label="关联查询的角色名称")
    String getUserRoles();
    void  setUserRoles(String userRoles);

    @Operator(Operator.IN)
    @Column(name = "`id`")
    List<String> getIds();
    void setIds(List<String> ids);
}
