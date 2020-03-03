package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.UserDataAuth;

import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/3 14:34
 */
@Restful("${uap.contextPath}")
public interface DataAuthRpc {

    /**
     * 根据条件查询用户数据权限表信息
     * @param userDataAuth	用户id必填
     * @return UserDataAuth列表
     */
    @POST("/dataAuthApi/listUserDataAuth.api")
    BaseOutput<List<UserDataAuth>> listUserDataAuth(@VOBody UserDataAuth userDataAuth);

    /**
     * 根据条件查询用户数据权限value列表
     * @param userDataAuth userId和refCode必填
     * @return
     */
    @POST("/dataAuthApi/listUserDataAuthValues.api")
    BaseOutput<List<String>> listUserDataAuthValues(@VOBody UserDataAuth userDataAuth);

    /**
     * 根据条件查询用户数据权限
     * @param userDataAuth userId和refCode必填
     * @return Map key为value, 值为转义后的行数据
     */
    @POST("/dataAuthApi/listUserDataAuthDetail.api")
    BaseOutput<List<Map>> listUserDataAuthDetail(@VOBody UserDataAuth userDataAuth);
}
