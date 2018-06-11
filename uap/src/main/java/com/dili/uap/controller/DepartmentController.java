package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.Department;
import com.dili.uap.domain.Firm;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.service.DepartmentService;
import com.dili.uap.service.FirmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-22 16:10:05.
 */
@Api("/department")
@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @Autowired
    FirmService firmService;

    @ApiOperation("跳转到Department页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        // 是否是集团
        boolean isGroup = SessionContext.getSessionContext().getUserTicket().getFirmCode().equalsIgnoreCase(UapConstants.GROUP_CODE);

        modelMap.addAttribute("isGroup", isGroup);
        modelMap.addAttribute("firmCode",SessionContext.getSessionContext().getUserTicket().getFirmCode());
        return "department/index";
    }

    @ApiOperation(value = "查询Department", notes = "查询Department，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Department", paramType = "form", value = "Department的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    Object list(Department department) {
        // 首次进入
        if (StringUtils.isEmpty(department.getFirmCode())) {
            boolean isGroup = SessionContext.getSessionContext().getUserTicket().getFirmCode().equalsIgnoreCase(UapConstants.GROUP_CODE);
            // 集团用户
            if (isGroup) {
                department.setFirmCode("group");
            } else {
                department.setFirmCode(SessionContext.getSessionContext().getUserTicket().getFirmCode());
            }
        } 
        List<Map> list = departmentService.listDepartments(department);
        return new EasyuiPageOutput(list.size(), list).toString();
//        return list;
    }

    @ApiOperation(value = "分页查询Department", notes = "分页查询Department，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Department", paramType = "form", value = "Department的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(Department department) throws Exception {
        return departmentService.listEasyuiPageByExample(department, true).toString();
    }

    @ApiOperation("新增Department")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Department", paramType = "form", value = "Department的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput insert(Department department) {
    	 Department input=this.trimIdAndParentId(department);
         BaseOutput<Department> out=departmentService.insertAfterCheck(input);
         return this.resetIdAndParentId(out);
    }

    @ApiOperation("修改Department")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Department", paramType = "form", value = "Department的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(Department department) {
    	Department input=this.trimIdAndParentId(department);
    	BaseOutput<Department> out=departmentService.updateAfterCheck(input);
    	return this.resetIdAndParentId(out);
    }

    @ApiOperation("删除Department")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "Department的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        departmentService.delete(id);
        return BaseOutput.success("删除成功");
    }

    @ApiOperation(value = "根据市场code查询Department", notes = "查询Department，返回列表信息")
    @RequestMapping(value = "/listByCondition.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Department> listByCondition(Department department) {
        return departmentService.list(department);
    }
    /**
     * 将id以及parentid的前辍去掉
     * @param department
     * @return
     */
    private Department trimIdAndParentId(Department department) {
    	if(department.aget("id")!=null) {
    		String id = department.aget("id").toString();
            if(id.startsWith("department_")){
            	department.setId(Long.parseLong(id.replace("department_", "")));
            }
    	}
        	if(department.aget("parentId")!=null) {
                String parentId = department.aget("parentId").toString();
                if(parentId.startsWith("department_")){
                	department.setParentId(Long.parseLong(parentId.replace("department_", "")));
                }else {
                	department.setParentId(null);
                }
        	}
         

        return department;
    }
    /**
     * 在返回数据前加上id以及parentId的前辍
     * @param department
     * @return
     */
    private BaseOutput<?> resetIdAndParentId(BaseOutput<Department>out) {
    	if(!out.isSuccess()) {
    		return out;
    	}
    	Department department=out.getData();
    		String id =department.getId().toString();
        	department.aset("id", "department_"+id);
        	
        	if(department.getParentId()!=null) {
                department.aset("parentId", "department_"+department.getParentId());
        	}else {
        		department.aset("parentId", "firm"+department.getFirmCode());
        	}
        	Object obj=DTOUtils.go(department);
        	 return BaseOutput.success().setData(obj);
    }
}