package com.dili.uap.sdk.domain.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.uap.sdk.domain.Department;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * <B>Description</B> <B>Copyright</B> 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/5/29 18:43
 */
public interface DepartmentDto extends Department {

	@Operator(Operator.IN)
	@Column(name = "`id`")
	List<String> getIds();

	void setIds(List<String> ids);

	/**
	 * 市场ID 根据市场ID查询部门 因为部门表，目前没有市场ID，所以无 Column 映射
	 * 
	 * @return
	 */
	@Transient
	Long getFirmId();

	void setFirmId(Long firmId);

	@Like(Like.BOTH)
	@Column(name = "`name`")
	String getNameLike();

	void setNameLike(String nameLike);
}
