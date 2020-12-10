package com.dili.uap.controller;

import com.alibaba.fastjson.JSONArray;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.domain.Position;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.validator.AddView;
import com.dili.uap.sdk.validator.ModifyView;
import com.dili.uap.service.FirmService;
import com.dili.uap.service.PositionService;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-12-01 15:23:34.
 */
@Controller
@RequestMapping("/position")
public class PositionController {
    @Autowired
    private PositionService positionService;
    @Autowired
    private FirmService firmService;

    /**
     * 跳转到Position页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Firm query = DTOUtils.newInstance(Firm.class);
        modelMap.put("firms", JSONArray.toJSONString(firmService.list(query)));
        return "position/index";
    }

    /**
     * 分页查询Position，返回easyui分页信息
     * @param position
     * @return String
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Position position) throws Exception {
        return positionService.listEasyuiPageByExample(position, true).toString();
    }

    /**
     * 根据市场code查询Position
     *
     * @param position
     * @return
     */
    @RequestMapping(value = "/listByCondition.action", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Position> listByCondition(Position position) {
        return positionService.list(position);
    }

    /**
     * 新增Position
     * @param position
     * @return BaseOutput
     */
    @BusinessLogger(businessType = "position_management", content = "新增职位", operationType = "add", systemCode = "UAP")
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@Validated(AddView.class) Position position) {
        String validator = (String) position.aget(IDTO.ERROR_MSG_KEY);
        if (StringUtils.isNotBlank(validator)) {
            return BaseOutput.failure(validator);
        }
        BaseOutput output = positionService.save(position);
        if (output.isSuccess()) {
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, position.getPositionName());
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, position.getId());
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (userTicket != null) {
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
            }
        }

        return output;
    }

    /**
     * 修改Position
     * @param position
     * @return BaseOutput
     */
    @BusinessLogger(businessType = "position_management", content = "修改职位", operationType = "edit", systemCode = "UAP")
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@Validated(ModifyView.class) Position position) {
        String validator = (String) position.aget(IDTO.ERROR_MSG_KEY);
        if (StringUtils.isNotBlank(validator)) {
            return BaseOutput.failure(validator);
        }
        BaseOutput output = positionService.save(position);
        if (output.isSuccess()) {
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, position.getPositionName());
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, position.getId());
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (userTicket != null) {
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
            }
        }

        return output;
    }

    /**
     * 删除Position
     * @param id
     * @return BaseOutput
     */
    @BusinessLogger(businessType = "position_management", content = "删除职位", operationType = "del", systemCode = "UAP")
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        Position position = positionService.get(id);
        BaseOutput<Object> output = positionService.deletePosition(id);
        if (output.isSuccess()) {
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, position.getPositionName());
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, position.getId());
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (userTicket != null) {
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
            }
        }
        return output;
    }
}