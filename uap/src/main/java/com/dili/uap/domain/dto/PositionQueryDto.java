package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.domain.Position;

import javax.persistence.Column;
import java.util.List;

/**
 * 职位查询DTO Created by ljf on 2020/12/08
 */
public interface PositionQueryDto extends Position {
    @Operator(Operator.IN)
    @Column(name = "`id`")
    List<String> getIds();

    void setIds(List<String> ids);
}
