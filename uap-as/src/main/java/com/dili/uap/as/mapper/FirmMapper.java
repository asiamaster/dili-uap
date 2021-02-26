package com.dili.uap.as.mapper;

import com.dili.ss.base.MyMapper;
import com.dili.uap.sdk.domain.Firm;

import java.util.List;

public interface FirmMapper extends MyMapper<Firm> {

	List<Firm> selectAllChildrenFirms(Long id);
}