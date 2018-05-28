package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.Firm;
import com.dili.uap.domain.Role;
import com.dili.uap.domain.dto.SystemResourceDto;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.RoleService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    private @Resource FirmService firmService;

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
        modelMap.put("firms", firmService.list(query));
        modelMap.put("isGroup", isGroup);
        modelMap.put("firmCode",firmCode);
        return "role/index";
    }

    @ApiOperation(value="查询Role", notes = "查询Role，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Role> list(@ModelAttribute Role role) {
        return roleService.list(role);
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
    public @ResponseBody BaseOutput insert(Role role) {
        role.setCreated(new Date());
        role.setModified(role.getCreated());
        return roleService.save(role).setData(role);
    }

    @ApiOperation("修改Role")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Role", paramType="form", value = "Role的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Role role) {
        Role updateRole = DTOUtils.newDTO(Role.class);
        updateRole.setId(role.getId());
        updateRole.setDescription(role.getDescription());
        updateRole.setRoleName(role.getRoleName());
        return roleService.save(role).setData(role);
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
}