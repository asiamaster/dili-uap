package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by asiam on 2018/5/22 0022.
 */
@Table(name = "`menu`")
public interface MenuCondition extends Menu {

    @Column(name = "`id`")
    @Operator(Operator.IN)
    List<Long> getIds();
    void setIds(List<Long> ids);

}
