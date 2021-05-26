package com.dili.uap.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.component.CustomerCharacterTypeSourceService;
import com.dili.uap.sdk.component.DataAuthSource;
import com.dili.uap.sdk.domain.DataAuthRef;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.dili.uap.service.DataAuthRefService;
import com.dili.uap.service.UserDataAuthService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据权限Api
 */
@Controller
@RequestMapping("/dataAuthApi")
public class DataAuthApi {

    @Autowired
    private UserDataAuthService userDataAuthService;

    @Autowired
    private DataAuthSource dataAuthSource;

    @Autowired
    private DataAuthRefService dataAuthRefService;

    @Autowired
    private CustomerCharacterTypeSourceService customerCharacterTypeSourceService;

    /**
     * 查询客户角色类型
     * @return
     */
    @PostMapping(value = "/listCustomerCharacterType.api")
    @ResponseBody
    public BaseOutput<List<Map<String, Object>>> listCustomerCharacterType(Long id) {
        List<Map<String, Object>> dataAuthes = customerCharacterTypeSourceService.listDataAuthes(null);
        List<String> selectUserDataAuthValue = userDataAuthService.listUserDataAuthValueByUserId(id, DataAuthType.CUSTOMER_TYPE.getCode());
        for (Map<String, Object> dataAuth : dataAuthes) {
            if(selectUserDataAuthValue.contains(dataAuth.get(CustomerCharacterTypeSourceService.NAME_KEY))){
                dataAuth.put("checked", true);
            }
        }
        return BaseOutput.successData(dataAuthes);
    }

    /**
     * 根据条件查询用户数据权限value列表
     * @param dataAuthRef
     * @return
     */
    @RequestMapping(value = "/listDataAuths.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<DataAuthRef>> listDataAuths(DataAuthRef dataAuthRef) {
        List<DataAuthRef> dataAuthRefList = dataAuthRefService.listByExample(dataAuthRef);
        return BaseOutput.success().setData(dataAuthRefList);
    }
    /**
     * 根据条件查询用户数据权限表信息
     * @param userDataAuth
     * @return
     */
    @RequestMapping(value = "/listUserDataAuth.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<UserDataAuth>> listUserDataAuth(UserDataAuth userDataAuth) {
        if(userDataAuth.getUserId() == null){
            return BaseOutput.failure("userId不能为空");
        }
        return BaseOutput.success().setData(userDataAuthService.listByExample(userDataAuth));
    }

    /**
     * 根据条件查询用户数据权限value列表
     * @param userDataAuth
     * @return
     */
    @RequestMapping(value = "/listUserDataAuthValues.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<String>> listUserDataAuthValues(UserDataAuth userDataAuth) {
        if(userDataAuth.getUserId() == null || StringUtils.isBlank(userDataAuth.getRefCode())){
            return BaseOutput.failure("userId和refCode不能为空");
        }
        List<UserDataAuth> userDataAuths = userDataAuthService.listByExample(userDataAuth);
        List<String> values = Lists.newArrayList();
        userDataAuths.stream().forEach(u -> {
            values.add(u.getValue());
        });
        return BaseOutput.success().setData(values);
    }

    /**
     * 根据条件查询用户数据权限
     * @param userDataAuth
     * @return  Map key为value, 值为转义后的行数据
     */
    @RequestMapping(value = "/listUserDataAuthDetail.api", method = { RequestMethod.POST })
    @ResponseBody
    public BaseOutput<List<Map>> listUserDataAuthDetail(UserDataAuth userDataAuth) {
        if(userDataAuth.getUserId() == null || StringUtils.isBlank(userDataAuth.getRefCode())){
            return BaseOutput.failure("userId和refCode不能为空");
        }
        List<UserDataAuth> userDataAuthList = userDataAuthService.listByExample(userDataAuth);
        //过滤获取不同的refCode
//        List<String> distinctRefCode = userDataAuthList.stream().map(t ->t.getRefCode()).distinct().collect(Collectors.toList());
        //key为refCode， value为values列表
        Map<String, List<String>> refCode2values = new HashMap<>();
        for(UserDataAuth uda : userDataAuthList){
            if(refCode2values.get(uda.getRefCode()) == null){
                refCode2values.put(uda.getRefCode(), Lists.newArrayList());
            }
            refCode2values.get(uda.getRefCode()).add(uda.getValue());
        }
//        Map key为数据权限id, 值为数据权限转义后的行数据
        List<Map<String, Map>> detailList = Lists.newArrayList();
        for(Map.Entry<String, List<String>> refCode2value : refCode2values.entrySet()){
            DataAuthRef dataAuthRef = DTOUtils.newInstance(DataAuthRef.class);
            dataAuthRef.setCode(refCode2value.getKey());
            List<DataAuthRef> dataAuthRefList = dataAuthRefService.list(dataAuthRef);
            if(dataAuthRefList == null || dataAuthRefList.isEmpty()){
                return null;
            }
            String springId = dataAuthRefList.get(0).getSpringId();
            DataAuthSourceService dataAuthSourceService = dataAuthSource.getDataAuthSourceServiceMap().get(springId);
            if(null == dataAuthSourceService){
                return null;
            }
            detailList.add(dataAuthSourceService.bindDataAuthes(dataAuthRef.getParam(), refCode2value.getValue()));
        }
        return BaseOutput.success().setData(detailList);
    }

    
    /**
     * 添加用户数据权限
     * @param json 包含reCode,value,userId
     * @return 
     */
	@ResponseBody
	@RequestMapping(value = "/addUserDataAuth.api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseOutput<Object> addUserDataAuth(@RequestBody String json) {
		// @RequestParam Long userId, @RequestParam String dataId, @RequestParam String
		// type
		JSONObject jo = JSON.parseObject(json);
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setRefCode(jo.getString("refCode"));
		userDataAuth.setValue(jo.getString("value"));
		userDataAuth.setUserId(jo.getLong("userId"));
		List<UserDataAuth> listByExample = this.userDataAuthService.listByExample(userDataAuth);
		if(listByExample!=null&&listByExample.size()>0) {
			return BaseOutput.failure("添加失败，value:" + jo.getString("value") + "和refCode:" + jo.getString("refCode") + "和userId:" + jo.getString("userId")+ ",不能找到唯一的数据权限");
		}
		userDataAuthService.insertSelective(userDataAuth);
		return BaseOutput.success("添加用户数据权成功");
	}
	/**
     * 删除用户数据权限
     * @param json 包含reCode,value,userId
     * @return 
     */
	@ResponseBody
	@RequestMapping(value = "/deleteUserDataAuth.api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public BaseOutput<Object> deleteUserDataAuth(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
		userDataAuth.setRefCode(jo.getString("refCode"));
		userDataAuth.setValue(jo.getString("value"));
		userDataAuth.setUserId(jo.getLong("userId"));
		List<UserDataAuth> listByExample = this.userDataAuthService.listByExample(userDataAuth);
		if(listByExample==null||listByExample.size()<1) {
			return BaseOutput.failure("删除失败，value:" + jo.getString("value") + "和refCode:" + jo.getString("refCode") + "和userId:" + jo.getString("userId")+ ",不能找到唯一的数据权限");
		}
		userDataAuthService.delete(userDataAuth);
		return BaseOutput.success("删除用户数据权成功");
	}
}