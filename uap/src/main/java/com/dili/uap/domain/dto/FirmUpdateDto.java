package com.dili.uap.domain.dto;

import com.dili.uap.sdk.domain.Firm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public interface FirmUpdateDto extends Firm {

	@NotBlank
	Long getId();

	@NotBlank
	String getCode();

	@NotBlank
	String getName();

	@NotBlank
	String getSimpleName();

	@NotNull
	String getIndustry();

	@NotBlank
	String getCertificateNumber();

	@NotNull
	Date getEffectTime();

	@NotNull
	Date getFailureTime();

	@NotNull
	Long getRegisteredProvinceId();

	@NotNull
	Long getRegisteredCityId();

	@NotNull
	Long getRegisteredDistrictId();

	@NotBlank
	String getRegisteredDetailAddress();

	@NotNull
	Long getActualProvinceId();

	@NotNull
	Long getActualCityId();

	@NotNull
	Long getActualDistrictId();

	@NotBlank
	String getActualDetailAddress();

	@NotBlank
	String getLegalPersonName();

	@NotBlank
	String getLegalPersonCertificateType();

	@NotBlank
	String getLegalPersonCertificateNumber();

	@NotNull
	Boolean getLongTermEffictive();

	@NotNull
	Long getDepositBank();

	@NotNull
	Long getDepositBankUnionInfoId();

	@NotBlank
	String getBankAccount();

	@NotBlank
	String getTelephone();

	@NotBlank
	String getEmail();

}
