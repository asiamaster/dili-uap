package com.dili.uap.sdk.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/24 17:46
 */
@Restful("${uap.contextPath}")
public interface UserRpc {

    @POST("/userApi/get.api")
    BaseOutput<User> get(@VOBody Long id);

    @POST("/userApi/list.api")
    BaseOutput<List<User>> list(@VOBody User user);

    @POST("/userApi/listByExample.api")
    BaseOutput<List<User>> listByExample(@VOBody User user);

    @POST("/userApi/listUserByIds.api")
    BaseOutput<List<User>> listUserByIds(@RequestBody List<String> ids);
}
