package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.domain.Resource;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

/**
 * 用于查询系统信息对象
 * Created by asiam on 2018/5/22 0022.
 */
@Table(name = "`resource`")
public interface ResourceDto extends Resource {
    @Column(name = "`id`")
    @Operator(Operator.IN)
    List<String> getIds();
    void setIds(List<String> ids);

    @Column(name = "`code`")
    @Operator(Operator.IN)
    List<String> getCodes();
    void setCodes(List<String> codes);
}
