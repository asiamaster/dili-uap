package com.dili.uap.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.domain.Menu;
import com.dili.uap.domain.Resource;
import com.dili.uap.glossary.MenuType;
import com.dili.uap.service.MenuService;
import com.dili.uap.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-21 16:08:04.
 */
@Api("/menu")
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private ResourceService resourceService;

    @ApiOperation("跳转到Menu页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "menu/index";
    }

    @RequestMapping(value = "/listSystemMenu.action", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Map> listSystemMenu() {
        return this.menuService.listSystemMenu();
    }

    @ApiOperation(value = "查询菜单列表", notes = "查询Menu，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", paramType = "Long", value = "menuId", required = false, dataType = "Long") })
    @RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody List<Menu> list(@RequestParam String menuId) {
        Menu query = DTOUtils.newDTO(Menu.class);
        if(menuId.startsWith("menu_")){
            query.setParentId(Long.parseLong(menuId.substring(5)));
        }else if(menuId.startsWith("sys_")){
            query.setSystemId(Long.parseLong(menuId.substring(4)));
            query.mset(IDTO.NULL_VALUE_FIELD, "parent_id");
        }
        query.setSort("order_number");
        query.setOrder("asc");
        return this.menuService.listByExample(query);
    }

    @ApiOperation(value = "查询内部链接列表", notes = "查询内部链接，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", paramType = "String", value = "menuId", required = false, dataType = "String")})
    @RequestMapping(value = "/listInternalLinks.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody List<Menu> listInternalLinks(@RequestParam String menuId) {
        Menu query = DTOUtils.newDTO(Menu.class);
        if(menuId.startsWith("menu_")){
            query.setParentId(Long.parseLong(menuId.substring(5)));
        }else if(menuId.startsWith("sys_")){
            query.setSystemId(Long.parseLong(menuId.substring(4)));
            query.mset(IDTO.NULL_VALUE_FIELD, "parent_id");
        }
        query.setType(MenuType.INTERNAL_LINKS.getCode());
        query.setSort("order_number");
        query.setOrder("asc");
        return this.menuService.listByExample(query);
    }

    @ApiOperation("新增Menu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Menu", paramType="form", value = "Menu的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Menu menu) {
        String menuId = menu.aget("menuId").toString();
        if(menuId.startsWith("menu_")){
            //如果菜单树上点的节点是菜单， 需要设置当前节点的父节点
            menu.setParentId(Long.parseLong(menuId.substring(5)));
        }else if(menuId.startsWith("sys_")){
            //如果菜单树上点的节点是系统，需要把parent_id清空
            menu.mset(IDTO.NULL_VALUE_FIELD, "parent_id");
        }
        menuService.insertSelective(menu);
        return BaseOutput.success("新增成功").setData(menu);
    }

    @ApiOperation("修改Menu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Menu", paramType="form", value = "Menu的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Menu menu) {
        String menuId = menu.aget("menuId").toString();
        if(menuId.startsWith("menu_")){
            //如果菜单树上点的节点(menuId)是菜单， 需要设置当前节点的父节点
            menu.setParentId(Long.parseLong(menuId.substring(5)));
        }
        Menu oldMenu = menuService.get(menu.getId());
        //如果修改了菜单类型，需要判断菜单下面不能有子菜单或资源
        if(!oldMenu.getType().equals(menu.getType())) {
            Menu condition = DTOUtils.newDTO(Menu.class);
            condition.setParentId(menu.getId());
            List children = menuService.list(condition);
            if (!CollectionUtils.isEmpty(children)) {
                return BaseOutput.failure("菜单有子节点，不允许修改类型");
            }
            Resource resouce = DTOUtils.newDTO(Resource.class);
            resouce.setMenuId(menu.getId());
            List<Resource> resources = resourceService.list(resouce);
            if (!CollectionUtils.isEmpty(resources)) {
                return BaseOutput.failure("菜单下有资源，不允许修改类型");
            }
        }
        menuService.updateExactSimple(menu);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Menu")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", paramType="form", value = "Menu的主键", required = true, dataType = "long")
    })
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        String msg = menuService.deleteMenu(id);
        return msg == null ? BaseOutput.success("删除成功") : BaseOutput.failure(msg);
    }

    /**
     * 拖动到菜单下面，需要修改parentId, 并且判断目标菜单是否目录，是目录则不处理menu类型，是链接则需要改为内键
     * 不允许跨系统拖动，不允许将目录拖到链接下面
     * @param sourceId
     * @param targetId
     * @return
     */
    @ApiOperation("移动Menu")
    @RequestMapping(value="/shiftMenu.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput shiftMenu(String sourceId, String targetId) {
        //源节点肯定是菜单，所以是以menu_开头
        Menu sourceMenu = menuService.get(Long.parseLong(sourceId.substring(5)));
        //如果拖动到系统下面，需要判断不允许跨系统拖动
        if(targetId.startsWith("sys_") && !sourceMenu.getSystemId().equals(Long.parseLong(targetId.substring(4)))){
            return BaseOutput.failure("不允许跨系统拖动");
        }
        //如果拖到系统下面, 需要清空parentId
        if(targetId.startsWith("sys_")){
            sourceMenu.setParentId(null);
            //修改内链菜单类型为链接
            if(sourceMenu.getType().equals(MenuType.INTERNAL_LINKS.getCode())) {
                sourceMenu.setType(MenuType.LINKS.getCode());
            }
        }else{
            //拖动到菜单下面，判断不允许跨系统拖动，不允许将目录拖动下链接下面
            //验证通过后需要修改parentId, 并且判断目标菜单是否目录，是目录则不处理menu类型，是链接则需要改为内键
            Long targetMenuId = Long.parseLong(targetId.substring(5));
            Menu targetMenu = menuService.get(targetMenuId);
            if(!targetMenu.getSystemId().equals(sourceMenu.getSystemId())){
                return BaseOutput.failure("不允许跨系统拖动");
            }
            //如果源是目录，目标非目录
            if(sourceMenu.getType().equals(MenuType.DIRECTORY.getCode()) && !targetMenu.getType().equals(MenuType.DIRECTORY.getCode())){
                return BaseOutput.failure("不允许将目录拖动到链接下面");
            }
            //如果源是链接，目标是链接或内链, 修改源为内链
            if(sourceMenu.getType().equals(MenuType.LINKS.getCode())
                    && (targetMenu.getType().equals(MenuType.INTERNAL_LINKS.getCode())
                    || targetMenu.getType().equals(MenuType.LINKS.getCode()))){
                sourceMenu.setType(MenuType.INTERNAL_LINKS.getCode());
            }
            //如果源是内链，目标是目录，修改源为链接
            if(sourceMenu.getType().equals(MenuType.INTERNAL_LINKS.getCode()) && targetMenu.getType().equals(MenuType.DIRECTORY.getCode())){
                sourceMenu.setType(MenuType.LINKS.getCode());
            }
            sourceMenu.setParentId(targetMenuId);
            if(targetMenu.getType().equals(MenuType.LINKS.getCode())){
                sourceMenu.setType(MenuType.INTERNAL_LINKS.getCode());
            }
        }
        menuService.updateExactSimple(sourceMenu);
        return BaseOutput.success("移动菜单成功");
    }

}