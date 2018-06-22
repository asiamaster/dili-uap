package com.dili.uap.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.Firm;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.dto.SystemResourceDto;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-18 11:45:41.
 */
@Api("/role")
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @Resource
    private FirmService firmService;

    @ApiOperation("跳转到Role页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        String firmCode = SessionContext.getSessionContext().getUserTicket().getFirmCode();
        //用户是否属于集团
        Boolean isGroup = false;
        Firm query = DTOUtils.newDTO(Firm.class);
        if (UapConstants.GROUP_CODE.equals(firmCode)) {
            isGroup = true;
        } else {
            query.setCode(firmCode);
        }
        modelMap.put("firms", JSONArray.toJSONString(firmService.list(query)));
        modelMap.put("isGroup", isGroup);
        modelMap.put("firmCode",firmCode);
        return "role/index";
    }

    @ApiOperation(value="查询Role", notes = "查询Role，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String list(Role role) throws Exception {
        List<Role> roleList = roleService.list(role);
        List<Role> sortList = roleList.stream().sorted(Comparator.comparing(r -> {
            return UapConstants.GROUP_CODE.equals(r.getFirmCode()) ? -1 : 1;
        })).collect(Collectors.toList());
        List<Map> list = ValueProviderUtils.buildDataByProvider(getRoleMetadata(), sortList);
        return JSONObject.toJSONString(list);
    }

    @ApiOperation(value="分页查询Role", notes = "分页查询Role，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Role role) throws Exception {
        return roleService.listEasyuiPageByExample(role, true).toString();
    }

    @ApiOperation("新增Role")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@Validated Role role) {
        String validator = (String) role.aget(IDTO.ERROR_MSG_KEY);
        if (StringUtils.isNotBlank(validator)){
            return BaseOutput.failure(validator);
        }
        role.setCreated(new Date());
        role.setModified(role.getCreated());
        return roleService.save(role).setData(role);
    }

    @ApiOperation("修改Role")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@Validated Role role) {
        String validator = (String) role.aget(IDTO.ERROR_MSG_KEY);
        if (StringUtils.isNotBlank(validator)){
            return BaseOutput.failure(validator);
        }
        Role updateRole = DTOUtils.newDTO(Role.class);
        updateRole.setId(role.getId());
        updateRole.setDescription(role.getDescription());
        updateRole.setRoleName(role.getRoleName());
        updateRole.setFirmCode(role.getFirmCode());
        return roleService.save(updateRole).setData(updateRole);
    }

    @ApiOperation("删除Role")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Role的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        return roleService.del(id);
    }

    @RequestMapping(value = "/getRoleMenuAndResource.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody String getRoleMenuAndResource(Long roleId) {
        List<SystemResourceDto> list = roleService.getRoleMenuAndResource(roleId);
        return new EasyuiPageOutput(list.size(), list).toString();
    }

    /**
     * 保存角色对应的资源权限信息
     * @param roleId 角色ID
     * @param resourceIds 资源ID集合
     * @return
     */
    @RequestMapping(value = "/saveRoleMenuAndResource.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody BaseOutput saveRoleMenuAndResource(Long roleId,String resourceIds[]) {
        return roleService.saveRoleMenuAndResource(roleId,resourceIds);
    }

    @ApiOperation("解绑Role和User")
    @RequestMapping(value = "/unbindRoleUser.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody BaseOutput unbindRoleUser(Long roleId,Long userId) {
        return roleService.unbindRoleUser(roleId,userId);
    }

    /**
     * 由于无法获取到表头上的meta信息，展示客户详情只有id参数，所以需要在后台构建
     * @return
     */
    private Map getRoleMetadata(){
        Map<Object, Object> metadata = new HashMap<>();
        //市场信息
        JSONObject firmCodeProvider = new JSONObject();
        firmCodeProvider.put("provider", "firmCodeProvider");
        firmCodeProvider.put(ValueProvider.FIELD_KEY, "firmCode");
        metadata.put("firmCode", firmCodeProvider);
        
        return metadata;
    }
}