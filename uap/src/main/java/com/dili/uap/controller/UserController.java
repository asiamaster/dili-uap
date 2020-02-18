package com.dili.uap.controller;

import com.alibaba.fastjson.JSONArray;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.constants.UapConstants;
import com.dili.uap.domain.DataAuthRef;
import com.dili.uap.domain.dto.UserDataDto;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.component.DataAuthSource;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.service.DataAuthSourceService;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.validator.AddView;
import com.dili.uap.sdk.validator.ModifyView;
import com.dili.uap.service.DataAuthRefService;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.UserDataAuthService;
import com.dili.uap.service.UserService;
import com.dili.uap.utils.PinYinUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-23 16:17:38.
 */
@Api("/user")
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private FirmService firmService;
    @Resource
    private UserDataAuthService userDataAuthService;

    @Autowired
    private DataAuthSource dataAuthSource;

    @Autowired
    private DataAuthRefService dataAuthRefService;
    @Value("${uap.adminName:admin}")
    private String adminName;
    /**
     * 跳转到User页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到User页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        String firmCode = SessionContext.getSessionContext().getUserTicket().getFirmCode();
        //用户是否属于集团
        Boolean isGroup = false;
        Firm query = DTOUtils.newInstance(Firm.class);
        if (UapConstants.GROUP_CODE.equals(firmCode)) {
            isGroup = true;
        } else {
            query.setCode(firmCode);
        }
        modelMap.put("firms", JSONArray.toJSONString(firmService.list(query)));
        modelMap.put("isGroup", isGroup);
        modelMap.put("firmCode",firmCode);
//        List users = new ArrayList<>(10000000);
//        for(int i=0; i<1_000_000;i++){
//            CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//            customizeBean.setId(3L);
//            customizeBean.setName("自定义Bean");
//            users.add(customizeBean);
//        }
//        System.out.println(users.size());
        return "user/index";
    }

    /**
     * 查询User
     * @param user
     * @return
     */
    @ApiOperation(value = "查询User", notes = "查询User，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<User> list(User user) {
        return userService.list(user);
    }

    /**
     * 分页查询User
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listPage(UserDto user) throws Exception {
//        long start = Systems.currentTimeMillis();
//        testStack1();
//        long end = Systems.currentTimeMillis();
//        System.out.println("1亿次Stack测试消耗1:"+ (end-start)+"ms");
//        testStack2();
//        System.out.println("1亿次Stack测试消耗2:"+ (Systems.currentTimeMillis()-end)+"ms");
//        end = Systems.currentTimeMillis();
//        testStack3();
//        System.out.println("1亿次Stack测试消耗3:"+ (Systems.currentTimeMillis()-end)+"ms");
//        end = Systems.currentTimeMillis();
//        testTlab1();
//        System.out.println("TLAB测试1消耗:"+ (Systems.currentTimeMillis()-end)+"ms");
//        end = Systems.currentTimeMillis();
//        testTlab2();
//        System.out.println("TLAB测试2消耗:"+ (Systems.currentTimeMillis()-end)+"ms");

        //easy rules测试代码
//        Rule rule = new MVELRule()
//                .name("myRule")
//                .description("myRuleDescription")
//                .priority(3)
//                .when("customizeBean.id==1")
//                .then("map.put(\"retVal\", userService.listEasyuiPage(user, true).toString());");
//        Rules rules = new Rules();
//        rules.register(rule);
//
//        Facts facts = new Facts();
//        CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//        customizeBean.setId(1L);
//        customizeBean.setName("customizeBean");
//        facts.put("customizeBean", customizeBean);
//        facts.put("user", user);
//        facts.put("userService", userService);
//        Map map = new HashMap(1);
//        facts.put("map", map);
//        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
//        RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
//        rulesEngine.fire(rules, facts);

        //硬编码规则测试代码
//        KieSession kieSession = kieContainer.newKieSession("test3");
//        kieSession = kieContainer.getKieBase("rules").newKieSession();
//        CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//        customizeBean.setId(1L);
//        customizeBean.setName("customizeBean");
//        customizeBean.setResult(false);
//        kieSession.insert(customizeBean);
//        int ruleFiredCount = kieSession.fireAllRules();
//        Collection c = kieSession.getObjects(new ObjectFilter() {
//            @Override
//            public boolean accept(Object object) {
////                System.out.println("aaaaaaaaaaaaaaaaa:"+object);
//                return true;
//            }
//        });
//        System.out.println(c);
//        System.out.println("触发了" + ruleFiredCount + "条规则");
//        if(customizeBean.getResult()){
//            System.out.println("规则校验通过");
//        }


//        KieSession kieSession = DroolsFactory.getChargingRulesKieBase().newKieSession();
//        CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//        customizeBean.setId(1L);
//        customizeBean.setName("customizeBean");
//        customizeBean.setResult(false);
//        FactHandle factHandle = kieSession.insert(customizeBean);
//        kieSession.fireAllRules();
//        kieSession.delete(factHandle);
//        kieSession.dispose();
        return userService.selectForEasyuiPage(user, true).toString();
    }

//    /**
//     * 栈对象测试1
//     */
//    private void testStack1() {
//        for (int i = 0; i < 100_000_000; i++) {
//            CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//            customizeBean.setId(1L);
//            customizeBean.setName("自定义Bean");
//        }
//    }
//
//    /**
//     * 栈对象测试2
//     */
//    private void testStack2() {
//        for (int i = 0; i < 100_000_000; i++) {
//            CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//            customizeBean.setId((long)i);
//            customizeBean.setName("自定义Bean");
//        }
//    }
//
//    /**
//     * 栈对象测试3
//     */
//    private void testStack3() {
//        for (int i = 0; i < 1_000_000; i++) {
//            for(int j=0; j<100; j++) {
//                CustomizeBeanImpl customizeBean = new CustomizeBeanImpl();
//                customizeBean.setId((long) j);
//                customizeBean.setName("自定义Bean");
//            }
//        }
//    }
//
//    /**
//     * TLAB测试
//     * @return
//     */
//    private void testTlab1() {
//        final int size = 5_000_000;
//        Object[] objects = new Object[size];
//        for (int c = 0; c < size; c++) {
//            objects[c] = new CustomizeBeanImpl();
//        }
////        return objects;
//    }
//
//    /**
//     * TLAB测试2
//     */
//    private void testTlab2() {
//        for (int i = 0; i < 5_000_000; i++) {
//            Object[] objects = new Object[1];
//            objects[0] = new CustomizeBeanImpl();
//        }
//    }

    /**
     * 新增User
     * @param user
     * @return
     */
    @ApiOperation("新增User")
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput insert(@Validated(AddView.class) User user) {
        String validator = (String) user.aget(IDTO.ERROR_MSG_KEY);
        if (StringUtils.isNotBlank(validator)) {
            return BaseOutput.failure(validator);
        }
        return userService.save(user);
    }

    /**
     * 修改User
     * @param user
     * @return
     */
    @ApiOperation("修改User")
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput update(@Validated(ModifyView.class) User user) {
        String validator = (String) user.aget(IDTO.ERROR_MSG_KEY);
        if (StringUtils.isNotBlank(validator)) {
            return BaseOutput.failure(validator);
        }
        return userService.save(user);
    }

    /**
     * 删除User
     * @param id
     * @return
     */
    @ApiOperation("删除User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "User的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput delete(Long id) {
        userService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 根据角色id查询User
     * @param roleId
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据角色id查询User", notes = "根据角色id查询User，返回列表信息")
    @RequestMapping(value = "/findUserByRole.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String findUserByRole(Long roleId,User user) throws Exception {
        List<User> retList = userService.findUserByRole(roleId);
        List results = ValueProviderUtils.buildDataByProvider(user, retList);
        return new EasyuiPageOutput(results.size(), results).toString();
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/changePwd.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput changePwd(UserDto user) {
        Long userId = SessionContext.getSessionContext().getUserTicket().getId();
        return userService.changePwd(userId, user);
    }

    /**
     * 根据姓名转换邮箱信息
     * @param name
     * @return
     */
    @ApiOperation(value = "根据姓名转换邮箱信息", notes = "根据姓名转换邮箱信息")
    @RequestMapping(value = "/getEmailByName.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput getEmailByName(String name) {
        return BaseOutput.success().setData(PinYinUtil.getFullPinYin(name.replace(" ","")) + UapConstants.EMAIL_POSTFIX);
    }

    /**
     * 重置密码
     * @param id
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequestMapping(value = "/resetPass.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput resetPass(Long id) {
        return userService.resetPass(id);
    }

    /**
     * 用户的禁启用
     * @param id
     * @param enable
     * @return
     */
    @ApiOperation(value = "用户的禁启用", notes = "用户的禁启用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "用户ID",dataType = "long"),
            @ApiImplicitParam(name = "enable", paramType = "path", value = "是否启用(true-启用)",dataType = "boolean")
    })
    @RequestMapping(value = "/doEnable.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput doEnable(Long id,Boolean enable){
         return userService.upateEnable(id,enable);
    }

    /**
     * 获取用户的角色信息
     * @param id
     * @return
     */
    @ApiOperation(value = "获取用户的角色信息", notes = "获取用户角色信息(tree结构)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "用户ID",dataType = "long"),
    })
    @RequestMapping(value = "/getUserRolesForTree.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public  String getUserRolesForTree(Long id){
        List list = userService.getUserRolesForTree(id);
        return JSONArray.toJSONString(list);
    }

    /**
     * 保存用户的角色信息
     * @param userId
     * @param roleIds
     * @return
     */
    @ApiOperation(value = "保存用户的角色信息", notes = "保存用户角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "path", value = "用户ID",dataType = "long"),
            @ApiImplicitParam(name = "roleIds", paramType = "path", value = "角色ID(包括市场ID)",dataType = "String"),
    })
    @RequestMapping(value = "/saveUserRoles.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput saveUserRoles(Long userId,String[] roleIds){
        return userService.saveUserRoles(userId,roleIds);
    }

    /**
     * 获取当前登录用户的信息
     * @return
     */
    @ApiOperation(value = "获取当前登录用户的信息", notes = "获取用户信息")
    @ApiImplicitParams({})
    @RequestMapping(value = "/fetchLoginUserInfo.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Object> fetchLoginUserInfo(){
    	Long userId = SessionContext.getSessionContext().getUserTicket().getId();
         return userService.fetchLoginUserInfo(userId);
    }

    /**
     * 获取数据权限
     * @param id
     * @return
     */
    @ApiOperation(value = "获取数据权限", notes = "获取用户数据权限")
    @RequestMapping(value = "/getUserData.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Map<String,Object>> getUserData(Long id){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("用户未登录");
        }
        BaseOutput<Map<String, Object>> output = BaseOutput.success();
        Map map = Maps.newHashMap();
        //获取数据权限的可选范围
        DataAuthRef dataAuthRef = DTOUtils.newInstance(DataAuthRef.class);
        dataAuthRef.setCode(DataAuthType.DATA_RANGE.getCode());
        List<DataAuthRef> dataAuthRefs = dataAuthRefService.list(dataAuthRef);

        DataAuthSourceService dataRangeSourceService = dataAuthSource.getDataAuthSourceServiceMap().get(dataAuthRefs.get(0).getSpringId());
        map.put("dataRange", dataRangeSourceService.listDataAuthes(dataAuthRefs.get(0).getParam()));
        //获取用户的数据权限
        map.put("userDatas", userService.getUserDataAuthForTree(id));
        //查询当前用户所属的权限范围
        UserDataAuth userDataAuth = DTOUtils.newInstance(UserDataAuth.class);
        userDataAuth.setUserId(id);
        userDataAuth.setRefCode(DataAuthType.DATA_RANGE.getCode());
        List<UserDataAuth> userDataAuths = userDataAuthService.list(userDataAuth);
        if (CollectionUtils.isNotEmpty(userDataAuths)) {
            map.put("currDataAuth", userDataAuths.get(0).getValue());
        }
        return output.setData(map);
    }
    
    /**
     * 获取项目数据权限
     * @param id
     * @return
     */
    @ApiOperation(value = "获取项目数据权限", notes = "获取用户项目数据权限")
    @RequestMapping(value = "/getUserProjectData.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Map<String,Object>> getUserProjectData(Long id){
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("用户未登录");
        }
        //获取需要分配数据权限的用户信息
        User user = userService.get(id);
        if (null == user) {
        	return BaseOutput.failure("没有该用户");
        }
        BaseOutput<Map<String, Object>> output = BaseOutput.success();
        Map map = Maps.newHashMap();
        map.put("dataProject",userService.getUserDataProjectAuthForTree(id));
        return output.setData(map);
    }

    /**
     * 保存用户的数据权限信息
     * @param userId
     * @param dataIds
     * @param dataRange
     * @return
     */
    @ApiOperation(value = "保存用户的数据权限信息", notes = "保存用户的数据权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", paramType = "path", value = "用户ID",dataType = "long"),
            @ApiImplicitParam(name = "dataIds", paramType = "path", value = "数据ID(包括市场ID)",dataType = "String"),
            @ApiImplicitParam(name = "dataRange", paramType = "path", value = "数据权限范围ID",dataType = "long"),
    })
    @RequestMapping(value = "/saveUserDatas.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput saveUserDatas(Long userId,String[] dataIds,Long dataRange){
        return userService.saveUserDatas(userId,dataIds,dataRange);
    }

    /**
     * 解锁用户
     * @param id
     * @return
     */
    @ApiOperation(value = "解锁用户", notes = "解锁用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", value = "用户ID",dataType = "long"),
    })
    @RequestMapping(value = "/unlock.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput unlock(Long id){
        return userService.unlock(id);
    }

    /**
     * 跳转到在线用户页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到在线用户页面")
    @RequestMapping(value = "/onlineList.html", method = RequestMethod.GET)
    public String onlineList(ModelMap modelMap) {
        String firmCode = SessionContext.getSessionContext().getUserTicket().getFirmCode();
        //用户是否属于集团
        Boolean isGroup = false;
        Firm query = DTOUtils.newInstance(Firm.class);
        if (UapConstants.GROUP_CODE.equals(firmCode)) {
            isGroup = true;
        } else {
            query.setCode(firmCode);
        }
        modelMap.put("firms", JSONArray.toJSONString(firmService.list(query)));
        modelMap.put("isGroup", isGroup);
        modelMap.put("firmCode",firmCode);
        return "user/onlineList";
    }

    /**
     * 分页查询在线用户
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询在线用户", notes = "分页查询在线用户，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", paramType = "form", value = "User的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listOnlinePage.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listOnlinePage(UserDto user) throws Exception {
        return userService.listOnlinePage(user).toString();
    }

    /**
     * 强制下线用户
     * @param id
     * @return
     */
    @ApiOperation("强制下线用户")
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "form", value = "User的主键", required = true, dataType = "long") })
    @RequestMapping(value = "/forcedOffline.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody BaseOutput forcedOffline(Long id) {
        return userService.forcedOffline(id);
    }

}