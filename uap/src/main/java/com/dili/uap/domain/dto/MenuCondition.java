package com.dili.uap.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
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

    @Column(name = "`type`")
    @FieldDef(label="类型")
    @Operator(Operator.IN)
    @EditMode(editor = FieldEditor.Combo, required = true, params="{\"data\":[{\"text\":\"目录\",\"value\":0},{\"text\":\"链接\",\"value\":1}]}")
    List<Integer> getTypes();
    void setTypes(List<Integer> types);
}
