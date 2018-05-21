package com.dili.uap.service.impl;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.domain.User;
import com.dili.uap.domain.dto.LoginResult;
import com.dili.uap.sdk.util.WebContent;
import com.dili.uap.service.LoginService;
import com.dili.uap.utils.WebUtil;
import org.springframework.stereotype.Component;

import static com.dili.uap.glossary.LoginConstants.COOKIE_SESSION_ID;

/**
 * 登录服务
 * Created by asiam on 2018/5/18 0018.
 */
@Component
public class LoginServiceImpl implements LoginService {
    @Override
    public BaseOutput<LoginResult> login(String userName, String password) {
        return null;
    }

    @Override
    public BaseOutput<Boolean> loginAndTag(String userName, String password) {
        return BaseOutput.success();
    }

    private void makeCookieTag(User user, String sessionId) {
        String referer = WebUtil.fetchReferer(WebContent.getRequest());
        WebContent.setCookie(COOKIE_SESSION_ID, sessionId);
        WebContent.setCookie("u", user.getId().toString());
        WebContent.setCookie("n", user.getUserName());
        WebContent.setCookie("loginPath", referer);
    }
}