package com.dili.uap.sdk.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.domain.Firm;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Table(name = "`firm`")
public interface FirmDto extends Firm {

    @Operator(Operator.IN)
    @Column(name = "`code`")
    @FieldDef(label="编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    List<String> getCodes();
    void setCodes(List<String> code);

}
