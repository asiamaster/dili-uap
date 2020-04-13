package com.dili.uap.sdk.domain.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.validator.AddView;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/4/13 15:45
 */
@Table(name = "`user`")
public interface UserQuery extends User {

    @Column(name = "`user_name`")
    @FieldDef(label="用户名", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    @NotNull(message = "用户名不能为空", groups = {AddView.class})
    @Size(min = 2, max = 20, message = "用户名称长度介于2-20之间", groups = {AddView.class})
    @Like
    @Override
    String getUserName();
    @Override
    void setUserName(String userName);

    @Operator(Operator.IN)
    @Column(name = "`id`")
    List<String> getIds();
    void setIds(List<String> ids);

    @Operator(Operator.IN)
    @Column(name = "`firm_code`")
    List<String> getFirmCodes();
    void setFirmCodes(List<String> firmCodes);
}
