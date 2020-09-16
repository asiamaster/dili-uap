package com.dili.uap.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.dto.PaymentFirmDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: 接入支付系统接口
 *
 * @date:    2020/9/16
 * @author:   Seabert.Zhan
 */
@FeignClient(name = "pay-service", contextId = "pay")
public interface PayRpc {

	/**
	 * 商户注册
	 *
	 * @param paymentFirmDto
	 * @return
	 */
	@RequestMapping(value = "/payment/spi/boss.do?service=payment.permit.register:merchant", method = RequestMethod.POST)
	BaseOutput<PaymentFirmDto> registerMerchant(@RequestBody PaymentFirmDto paymentFirmDto);

	/**
	 * 修改商户
	 *
	 * @param paymentFirmDto
	 * @return
	 */
	@RequestMapping(value = "/payment/spi/boss.do?service=payment.permit.register:modifyMerchant", method = RequestMethod.POST)
    BaseOutput<PaymentFirmDto> modifyMerchant(@RequestBody PaymentFirmDto paymentFirmDto);
}
