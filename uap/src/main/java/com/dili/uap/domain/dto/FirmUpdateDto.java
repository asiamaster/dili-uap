package com.dili.uap.domain.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dili.ss.dto.IDTO;

public interface FirmUpdateDto extends IDTO {

	@NotBlank
	Long getId();

	void setId(Long id);

	@NotBlank
	String getName();

	void setName(String name);

	@NotBlank
	String getSimpleName();

	void setSimpleName(String simpleName);

	Long getParentId();

	void setParentId(Long parentId);

	String getDescription();

	@NotNull
	String getIndustry();

	void setIndustry(String industry);

	String getCertificateType();

	void setCertificateType(String certificateType);

	@NotBlank
	String getCertificateNumber();

	void setCertificateNumber(String certificateNumber);

	@NotNull
	Date getEffectTime();

	void setEffectTime(Date effectTime);

	@NotNull
	Date getFailureTime();

	void setFailureTime(Date failureTime);

	@NotNull
	Long getRegisteredProvinceId();

	void setRegisteredProvinceId(Long registeredProvinceId);

	@NotNull
	Long getRegisteredCityId();

	void setRegisteredCityId(Long registeredCityId);

	@NotNull
	Long getRegisteredDistrictId();

	void setRegisteredDistrictId(Long registeredDistrictId);

	@NotBlank
	String getRegisteredDetailAddress();

	void setRegisteredDetailAddress(String registeredDetailAddress);

	@NotNull
	Long getActualProvinceId();

	void setActualProvinceId(Long actualProvinceId);

	@NotNull
	Long getActualCityId();

	void setActualCityId(Long actualCityId);

	@NotNull
	Long getActualDistrictId();

	void setActualDistrictId(Long actualDistrictId);

	@NotBlank
	String getActualDetailAddress();

	void setActualDetailAddress(String actualDetailAddress);

	@NotBlank
	String getLegalPersonName();

	void setLegalPersonName(String legalPersonName);

	@NotBlank
	String getLegalPersonCertificateType();

	void setLegalPersonCertificateType(String legalPersonCertificateType);

	@NotBlank
	String getLegalPersonCertificateNumber();

	void setLegalPersonCertificateNumber(String legalPersonCertificateNumber);

	@NotNull
	Date getCertificateValidityPeriod();

	void setCertificateValidityPeriod(Date certificateValidityPeriod);

	@NotNull
	Boolean getLongTermEffictive();

	void setLongTermEffictive(Boolean longTermEffictive);

	@NotNull
	Long getDepositBank();

	void setDepositBank(Long depositBank);

	@NotNull
	Long getDepositBankUnionInfoId();

	void setDepositBankUnionInfoId(Long depositBankUnionInfoId);

	@NotBlank
	String getBankAccount();

	void setBankAccount(String bankAccount);

	@NotBlank
	String getTelephone();

	void setTelephone(String telephone);

	@NotBlank
	String getEmail();

	void setEmail(String email);

}
