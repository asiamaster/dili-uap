package com.dili.uap.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.assets.sdk.dto.BankDto;
import com.dili.assets.sdk.dto.CityDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.rpc.BankRpc;

/**
 * <B>Description</B> <B>Copyright</B> 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/29 18:32
 */
@Component
public class BankProvider implements ValueProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(BankProvider.class);

	@Autowired
	private BankRpc bankRpc;

	@Override
	public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
		return null;
	}

	@Override
	public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
		if (val == null) {
			return "";
		}
		BankDto query = new BankDto();
		query.setId(Long.valueOf(val.toString()));
		BaseOutput<List<BankDto>> output = this.bankRpc.list(query);
		if (!output.isSuccess()) {
			LOGGER.error(output.getMessage());
			return null;
		}
		return output.getData().get(0).getName();
	}

}
