package com.dili.uap.domain.dto;

import java.util.List;

/**
 * Description: 商户注册/修改商户参数类
 *
 * @date: 2020/9/16
 * @author: Seabert.Zhan
 *
 */
public class PaymentFirmDto {
	/**
	 * 商户ID
	 */
	private Long mchId;

	/**
	 * 父级市场id
	 */
	private Long parentId;

	/**
	 * 商户编码
	 */
	private String code;

	/**
	 * 商户名称
	 */
	private String name;

	/**
	 * 商户地址
	 */
	private String address;

	/**
	 * 商户联系人
	 */
	private String contact;

	/**
	 * 联系人手机
	 */
	private String mobile;

	/**
	 * 商户账户的支付密码
	 */
	private String password;

	/**
	 * 收益账号
	 */
	private Long profitAccount;

	/**
	 * 担保账户
	 */
	private Long vouchAccount;

	/**
	 * 押金账户
	 */
	private Long pledgeAccount;

	/**
	 * 分配给商户的平台私钥-Base64
	 */
	private String privateKey;

	/**
	 * 分配给商户的平台公钥-Base64
	 */
	private String publicKey;

	public Long getMchId() {
		return mchId;
	}

	public void setMchId(Long mchId) {
		this.mchId = mchId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getProfitAccount() {
		return profitAccount;
	}

	public void setProfitAccount(Long profitAccount) {
		this.profitAccount = profitAccount;
	}

	public Long getVouchAccount() {
		return vouchAccount;
	}

	public void setVouchAccount(Long vouchAccount) {
		this.vouchAccount = vouchAccount;
	}

	public Long getPledgeAccount() {
		return pledgeAccount;
	}

	public void setPledgeAccount(Long pledgeAccount) {
		this.pledgeAccount = pledgeAccount;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}
