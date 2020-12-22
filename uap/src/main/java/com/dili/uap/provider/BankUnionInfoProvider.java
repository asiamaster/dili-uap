package com.dili.uap.provider;

import com.dili.assets.sdk.dto.BankUnionInfoDto;
import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.rpc.CityRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.rpc.BankUnionInfoRpc;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.service.DepartmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>Description</B> <B>Copyright</B> 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/29 18:32
 */
@Component
public class BankUnionInfoProvider extends BatchDisplayTextProviderAdaptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BankUnionInfoProvider.class);

	@Autowired
	private BankUnionInfoRpc bankUnionInfoRpc;

	@Override
	protected List getFkList(List<String> ids, Map map) {
		BankUnionInfoDto bankDto = new BankUnionInfoDto();
		BaseOutput<List<BankUnionInfoDto>> output = bankUnionInfoRpc.list(bankDto);
		if (!output.isSuccess()) {
			LOGGER.error(output.getMessage());
			return null;
		}
		return output.getData();
	}

	@Override
	protected Map<String, String> getEscapeFileds(Map metaMap) {
		if (metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map) {
			return (Map) metaMap.get(ESCAPE_FILEDS_KEY);
		} else {
			Map<String, String> map = new HashMap<>();
			map.put(metaMap.get(FIELD_KEY).toString(), "bankName");
			return map;
		}
	}

	@Override
	protected String getFkField(Map metaMap) {
		String fkField = (String) metaMap.get(FK_FILED_KEY);
		return fkField == null ? "bankId" : fkField;
	}
}