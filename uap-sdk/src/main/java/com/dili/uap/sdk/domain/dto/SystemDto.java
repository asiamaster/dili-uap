package com.dili.uap.sdk.domain.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.sdk.domain.Systems;

import javax.persistence.Column;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/13 16:08
 */
public interface SystemDto extends Systems {
    @Column(name = "`name`")
    @Like(Like.BOTH)
    String getLikeName();
    void setLikeName(String likeName);

    /**
     * 编码集合
     * @return
     */
    @Column(name = "`code`")
    @Operator(Operator.IN)
    List<String> getCodeList();
    void setCodeList(List<String> codeList);
}
