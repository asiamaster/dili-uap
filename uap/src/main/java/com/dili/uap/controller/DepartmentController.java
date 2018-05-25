package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
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
        boolean isGroup = SessionContext.getSessionContext().getUserTicket().getFirmCode().endsWith(UapConstants.GROUP_CODE);

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
    List<Department> list(Department department) {

        Department root = DTOUtils.newDTO(Department.class);

        // 首次进入
        if (StringUtils.isEmpty(department.getFirmCode())) {
            List<Firm> list;
            boolean isGroup = SessionContext.getSessionContext().getUserTicket().getFirmCode().endsWith(UapConstants.GROUP_CODE);

            // 集团用户
            if (isGroup) {
                list = firmService.list(null);
            } else {
                Firm t = DTOUtils.newDTO(Firm.class);
                t.setCode(SessionContext.getSessionContext().getUserTicket().getFirmCode());
                list = firmService.list(t);
            }


            Firm firm = list.get(0);
            root.setId(-1L);
            root.setName(firm.getName());
            root.setFirmCode(firm.getCode());
            department.setFirmCode(firm.getCode());
        } else {
            // 切换市场
            Firm t = DTOUtils.newDTO(Firm.class);
            t.setCode(department.getFirmCode());
            List<Firm> list = firmService.list(t);
            Firm firm = list.get(0);
            root.setId(-1L);
            root.setName(firm.getName());
            root.setFirmCode(firm.getCode());
        }

        List<Department> list = departmentService.list(department);

        list.add(root);

        return list;
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
        return departmentService.insertAfterCheck(department);
    }

    @ApiOperation("修改Department")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Department", paramType = "form", value = "Department的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(Department department) {
        return departmentService.updateAfterCheck(department);
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
}