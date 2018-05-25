package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.domain.Firm;

import javax.persistence.Column;
import java.util.List;

/**
 * 公司查询DTO
 * Created by asiam on 2018/5/25 0025.
 */
public interface FirmQueryDto extends Firm{

    @Operator(Operator.IN)
    @Column(name = "`id`")
    List<String> getIds();
    void setIds(List<String> ids);

    @Operator(Operator.IN)
    @Column(name = "`code`")
    List<String> getCodes();
    void setCodes(List<String> codes);

}
