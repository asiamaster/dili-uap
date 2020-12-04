package com.dili.uap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.dao.DataDictionaryMapper;
import com.dili.uap.dao.DataDictionaryValueMapper;
import com.dili.uap.dao.FirmMapper;
import com.dili.uap.domain.DataDictionaryValueState;
import com.dili.uap.domain.dto.DataDictionaryDto;
import com.dili.uap.sdk.domain.DataDictionary;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.service.DataDictionaryValueService;
import com.dili.uap.service.FirmService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-05-21 10:40:13.
 */
@Transactional
@Service
public class DataDictionaryValueServiceImpl extends BaseServiceImpl<DataDictionaryValue, Long> implements DataDictionaryValueService {

	public DataDictionaryValueMapper getActualDao() {
		return (DataDictionaryValueMapper) getDao();
	}

	@Autowired
	private DataDictionaryMapper dataDictionaryMapper;
	@Autowired
	private DataDictionaryValueMapper ddValueMapper;
	@Autowired
	private FirmMapper firmMapper;
	@Autowired
	private FirmService firmService;

	@Override
	public List<DataDictionaryValue> listDictionaryValueByCode(String code) {
		DataDictionaryValue query = DTOUtils.newInstance(DataDictionaryValue.class);
		query.setDdCode(code);
		return this.ddValueMapper.select(query);
	}

	@Override
	public BaseOutput<Object> insertAfterCheck(DataDictionaryValue t) {

		if (StringUtils.isBlank(t.getCode())) {
			return BaseOutput.failure("编码不能为空");
		}
		if (StringUtils.isBlank(t.getDdCode())) {
			return BaseOutput.failure("数据字典编码不能为空");
		}
		DataDictionaryValue condition = DTOUtils.newInstance(DataDictionaryValue.class);
		condition.setCode(StringUtils.trim(t.getCode()));
		condition.setFirmCode(t.getFirmCode());
		condition.setDdCode(StringUtils.trim(t.getDdCode()));
		int size = this.list(condition).size();
		if (size > 0) {
			return BaseOutput.failure("相同编码已经存在");
		}
		if (StringUtils.isNotBlank(t.getFirmCode())) {
			Firm firmQuery = DTOUtils.newInstance(Firm.class);
			firmQuery.setCode(t.getFirmCode());
			Firm firm = this.firmMapper.selectOne(firmQuery);
			if (firm != null) {
				t.setFirmId(firm.getId());
			}
		}
		this.insertSelective(t);
		return BaseOutput.success("新增成功").setData(t.getId());
	}

	@Override
	public BaseOutput<Object> updateAfterCheck(DataDictionaryValue t) {
		if (StringUtils.isBlank(t.getCode())) {
			return BaseOutput.failure("编码不能为空");
		}
		if (StringUtils.isBlank(t.getDdCode())) {
			return BaseOutput.failure("系统编码不能为空");
		}
		DataDictionaryValue condition = DTOUtils.newInstance(DataDictionaryValue.class);
		condition.setCode(StringUtils.trim(t.getCode()));
		condition.setFirmCode(t.getFirmCode());
		condition.setDdCode(StringUtils.trim(t.getDdCode()));
		boolean exists = this.list(condition).stream().anyMatch((d) -> !d.getId().equals(t.getId()));
		if (exists) {
			return BaseOutput.failure("相同编码已经存在");
		}
		if (StringUtils.isNotBlank(t.getFirmCode())) {
			Firm firmQuery = DTOUtils.newInstance(Firm.class);
			firmQuery.setCode(t.getFirmCode());
			Firm firm = this.firmMapper.selectOne(firmQuery);
			if (firm != null) {
				t.setFirmId(firm.getId());
			}
		}
		this.updateSelective(t);
		return BaseOutput.success("修改成功");
	}

	@Override
	public DataDictionaryDto findByCode(String code, String systemCode) {
		return this.findByCode(code, systemCode, null);
	}

	@Override
	@Transactional
	public BaseOutput<Object> insertDataDictionaryDto(DataDictionaryDto dataDictionaryDto) {
		try {
			DataDictionary dataDictionary = DTOUtils.as(dataDictionaryDto, DataDictionary.class);
			int insert = this.dataDictionaryMapper.insert(dataDictionary);
			if (insert == 0) {
				throw new RuntimeException("添加失败");
			}
			List<DataDictionaryValue> deDataDictionaryValues = dataDictionaryDto.getDataDictionaryValues();
			for (DataDictionaryValue dataDictionaryValue : deDataDictionaryValues) {
				int insertValue = this.getActualDao().insert(dataDictionaryValue);
				if (insertValue == 0) {
					throw new RuntimeException("添加失败");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return BaseOutput.failure(e.getMessage());
		}

		return BaseOutput.success("添加成功");
	}

	@Override
	public DataDictionaryDto findByCode(String code, String systemCode, String firmCode) {
		DataDictionary record = DTOUtils.newInstance(DataDictionary.class);
		record.setCode(code);
		record.setSystemCode(systemCode);
		DataDictionary model = this.dataDictionaryMapper.selectOne(record);
		if (model == null) {
			return null;
		}
		DataDictionaryValue valueRecord = DTOUtils.newInstance(DataDictionaryValue.class);
		valueRecord.setDdCode(model.getCode());
		if (StringUtils.isBlank(firmCode)) {
			valueRecord.setMetadata(IDTO.AND_CONDITION_EXPR, "(firm_code = '" + UapConstants.GROUP_CODE + "' OR firm_code IS NULL)");
		} else {
			valueRecord.setFirmCode(StringUtils.isBlank(firmCode) ? UapConstants.GROUP_CODE : firmCode);
		}
		List<DataDictionaryValue> values = this.listByExample(valueRecord);
		DataDictionaryDto dto = DTOUtils.as(model, DataDictionaryDto.class);
		if (CollectionUtils.isNotEmpty(values)) {
			List<DataDictionaryValue> dtos = new ArrayList<>(values.size());
			for (DataDictionaryValue value : values) {
				dtos.add(value);
			}
			dto.setDataDictionaryValues(dtos);
		}
		return dto;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseOutput<Object> synchronizeToOrtherFirms(Long id) {
		DataDictionaryValue ddVal = this.getActualDao().selectByPrimaryKey(id);
		Date now = new Date();
		if (StringUtils.isEmpty(ddVal.getFirmCode())) {
			Firm groupFirm = this.firmService.getByCode(UapConstants.GROUP_CODE);
			ddVal.setFirmCode(UapConstants.GROUP_CODE);
			ddVal.setFirmId(groupFirm.getId());
			ddVal.setModified(now);
			this.getActualDao().updateByPrimaryKeySelective(ddVal);
		}
		List<Firm> firms = this.firmMapper.selectAll();
		DataDictionaryValue ddValueQuery = DTOUtils.newInstance(DataDictionaryValue.class);
		ddValueQuery.setDdCode(ddVal.getDdCode());
		ddValueQuery.setCode(ddVal.getCode());
		List<DataDictionaryValue> values = this.getActualDao().select(ddValueQuery);
		List<DataDictionaryValue> insertList = new ArrayList<DataDictionaryValue>();
		firms.forEach(f -> {
			if (values.stream().filter(v -> {
				if (StringUtils.isBlank(v.getFirmCode())) {
					Firm groupFirm = this.firmService.getByCode(UapConstants.GROUP_CODE);
					v.setFirmCode(UapConstants.GROUP_CODE);
					v.setFirmId(groupFirm.getId());
					this.firmMapper.updateByPrimaryKeySelective(groupFirm);
				}
				return v.getFirmCode().equals(f.getCode());
			}).findFirst().orElse(null) == null) {
				DataDictionaryValue value = DTOUtils.newInstance(DataDictionaryValue.class);
				value.setCode(ddVal.getCode());
				value.setCreated(new Date());
				value.setDdCode(ddVal.getDdCode());
				value.setDescription(ddVal.getDescription());
				value.setFirmCode(f.getCode());
				value.setFirmId(f.getId());
				value.setName(ddVal.getName());
				value.setParentId(ddVal.getParentId());
				value.setState(DataDictionaryValueState.ENABLED.getValue());
				value.setCreated(now);
				value.setModified(now);
				insertList.add(value);
			}
		});
		if (CollectionUtils.isNotEmpty(insertList)) {
			this.getActualDao().insertList(insertList);
		}
		return BaseOutput.success();
	}

}