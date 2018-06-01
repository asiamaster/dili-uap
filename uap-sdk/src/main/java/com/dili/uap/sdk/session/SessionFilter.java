package com.dili.uap.sdk.session;


import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Menu;
import com.dili.uap.sdk.domain.SystemExceptionLog;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotAccessPermission;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.exception.RedirectException;
import com.dili.uap.sdk.glossary.ExceptionType;
import com.dili.uap.sdk.redis.UserUrlRedis;
import com.dili.uap.sdk.rpc.MenuRpc;
import com.dili.uap.sdk.rpc.SystemExceptionLogRpc;
import com.dili.uap.sdk.util.WebContent;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 权限统一拦截器
 */
@Component
public class SessionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SessionFilter.class);
    //配置信息
    @Autowired
    private ManageConfig config;
    //过滤器配置，留待使用
    private FilterConfig filterConfig;

    @Autowired
    private MenuRpc menuRpc;

    @Autowired
    private UserUrlRedis userResRedis;

    @Autowired
    private SystemExceptionLogRpc systemExceptionLogRpc;

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig conf) throws ServletException {
        filterConfig = conf;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filter) throws IOException, ServletException {
        WebContent.resetLocal();
        SessionContext.resetLocal();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        WebContent.put(req);
        WebContent.put(resp);
        PermissionContext pc = new PermissionContext(req, resp, this, config);
        WebContent.put(SessionConstants.MANAGE_PERMISSION_CONTEXT, pc);
        //如果是框架导出，需要手动设置SessionId到Header中，因为restful取不到cookies
        if(pc.getReq().getRequestURI().trim().endsWith("/export/serverExport")) {
            MutableHttpServletRequest mreq = new MutableHttpServletRequest(req);
            mreq.putHeader(SessionConstants.SESSION_ID, pc.getSessionId());
            req = mreq;
        }
        //判断是否要(include或exclude)权限检查, 不过滤就直接放过
        if (!config.hasChecked()) {
            filter.doFilter(req, resp);
            return;
        }
        proxyHandle(req, resp, filter);
    }

    /**
     * 权限判断
     * @param request
     * @param response
     * @param filter
     * @throws IOException
     */
    private void proxyHandle(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws IOException {
        WebContent.put(request);
        WebContent.put(response);
        PermissionContext pc = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
        if (log.isDebugEnabled()) {
            log.debug("请求地址:" + pc.getUrl());
        }
        try {
            //iframe
            checkIframe(pc);
            //登录
            checkUser(pc);
            setNavAttr(pc);
            filter.doFilter(request, response);
        } catch (RedirectException e) {
            pc.sendRedirect(e.getPath());
        } catch (NotLoginException e) {
            pc.noAccess();
            systemExceptionLog(pc, e);
        } catch (NotAccessPermission e) {
            if (log.isInfoEnabled()) {
                log.info("用户{Session:" + pc.getSessionId() + ", userId:" + pc.getUserId() + "}没有访问" + pc.getUrl() + "权限！");
            }
            pc.nonPermission();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            SessionContext.remove();
        }
    }

    /**
     * 记录系统异常
     * @param pc
     * @param e
     */
    private void systemExceptionLog(PermissionContext pc, Exception e){
        BaseOutput<Map<String, Object>> output = menuRpc.getMenuDetailByUrl(pc.getUrl());
        if(output.isSuccess()){
            Map<String, Object> menu1 = output.getData();
            if(menu1 == null || menu1.isEmpty()) {
                return;
            }
            SystemExceptionLog systemExceptionLog = DTOUtils.newDTO(SystemExceptionLog.class);
            systemExceptionLog.setMenuId(Long.parseLong(menu1.get("id").toString()));
            systemExceptionLog.setMsg(e.getMessage());
            systemExceptionLog.setSystemCode(menu1.get("system_code").toString());
            systemExceptionLog.setSystemName(menu1.get("system_name").toString());
            systemExceptionLog.setType(ExceptionType.NOT_AUTH_ERROR.getCode());
            systemExceptionLog.setExceptionTime(new Date());
            systemExceptionLog.setIp(pc.getReq().getRemoteAddr());
            systemExceptionLogRpc.insert(systemExceptionLog);
        }
    }

    /**
     * 检查是否在iframe中
     * @param pc
     */
    private void checkIframe(PermissionContext pc){
        String referer = pc.getReferer();
        if (StringUtils.equals("/main.do", pc.getUri())) {
            return;
        }
        if (!pc.getConfig().getMustIframe()) {
            return;
        }
        if (!StringUtils.isBlank(referer)) {
            return;
        }
        if(pc.getHandler().getClass().getName().equalsIgnoreCase("ResourceHttpRequestHandler")){
            return;
        }
        StringBuffer path = new StringBuffer();
        path.append("/main.do?returnUrl=");
        path.append(pc.getUrl());
        path.append("?");
        path.append(pc.getQueryString());
        throw new RedirectException(path.toString());
    }

    /**
     * 检查用户是否有权限访问
     * @param pc
     */
    private void checkUser(PermissionContext pc) {
        if (pc.getSessionId() == null) {
            throw new NotLoginException();
        }
        UserTicket user = pc.getUser();
        if (user == null) {
            throw new NotLoginException();
        }
        if (userResRedis.checkUserMenuUrlRight(user.getId(), pc.getUrl())) {
            return;
        }
        //检测授权
        UserTicket auth = pc.getAuthorizer();
        if(auth == null){
            throw new NotAccessPermission();
        }
        throw new NotAccessPermission();
    }

    /**
     * 设置导航栏需要的数据到request
     * @param pc
     */
    private void setNavAttr(PermissionContext pc){
        BaseOutput<List<Menu>> list = menuRpc.getParentMenusByUrl(pc.getUrl());
        if(list == null || list.getData() == null || list.getData().isEmpty()){
            WebContent.getRequest().setAttribute("parentMenus", Lists.newArrayList());
        }else {
            WebContent.getRequest().setAttribute("parentMenus", list.getData());
        }
    }

    final class MutableHttpServletRequest extends HttpServletRequestWrapper {
        // holds custom header and value mapping
        private final Map<String, String> customHeaders;

        public MutableHttpServletRequest(HttpServletRequest request){
            super(request);
            this.customHeaders = new HashMap<String, String>();
        }

        public void putHeader(String name, String value){
            this.customHeaders.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            // check the custom headers first
            String headerValue = customHeaders.get(name);

            if (headerValue != null){
                return headerValue;
            }
            // else return from into the original wrapped object
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            // create a set of the custom header names
            Set<String> set = new HashSet<String>(customHeaders.keySet());

            // now add the headers from the wrapped request object
            @SuppressWarnings("unchecked")
            Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
            while (e.hasMoreElements()) {
                // add the names of the request headers into the list
                String n = e.nextElement();
                set.add(n);
            }

            // create an enumeration from the set and return
            return Collections.enumeration(set);
        }
    }

}
