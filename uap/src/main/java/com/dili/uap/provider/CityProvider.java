package com.dili.uap.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.assets.sdk.dto.CityDto;
import com.dili.assets.sdk.rpc.CityRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.BatchProviderMeta;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderSupport;

/**
 * <B>Description</B> <B>Copyright</B> 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/29 18:32
 */
@Component
public class CityProvider extends BatchDisplayTextProviderSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(CityProvider.class);

	@Autowired
	private CityRpc cityRpc;

	@Override
	protected List getFkList(List<String> ids, Map map) {
		CityDto cityDto = new CityDto();
		BaseOutput<List<CityDto>> output = cityRpc.list(cityDto);
		if (!output.isSuccess()) {
			LOGGER.error(output.getMessage());
			return null;
		}
		return output.getData();
	}

	@Override
	protected BatchProviderMeta getBatchProviderMeta(Map metaMap) {
		BatchProviderMeta bpm = DTOUtils.newInstance(BatchProviderMeta.class);
		bpm.setFkField("registeredDistrictId");
		bpm.setEscapeFiled("name");
		return bpm;
	}
}
