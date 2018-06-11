package com.dili.uap.sdk.util;

import com.dili.uap.sdk.session.ManageConfig;
import com.dili.uap.sdk.session.PermissionContext;
import com.dili.uap.sdk.session.SessionConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * MVC容器
 * User: juqkai (juqkai@gmail.com)
 * Date: 13-9-25 上午11:12
 */
public class WebContent {
    private static Pattern ip = Pattern.compile("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");

    private static ThreadLocal<Map<String, Object>> local = new ThreadLocal<Map<String, Object>>();

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) get("req");
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) get("resp");
    }

    /**
     * 获得权限上下文
     * @return
     */
    public static PermissionContext getPC(){
        return (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
    }

    /**
     * 获取配置对象
     * @return
     */
    public static ManageConfig getMC(){
        return getPC().getConfig();
    }

    public static void resetLocal(){
        local.remove();
    }

    /**
     * 得到容器
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        HttpServletRequest req = (HttpServletRequest) get("req");
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
        if (context == null) {

            ServletContext sc = req.getSession().getServletContext();
            Enumeration e = sc.getAttributeNames();
            while (e.hasMoreElements()) {
                String name = e.nextElement().toString();
                if (name.startsWith(FrameworkServlet.SERVLET_CONTEXT_PREFIX)) {
                    if (sc.getAttribute(name) instanceof ApplicationContext) {
                        context = (ApplicationContext) sc.getAttribute(name);
                        return context;
                    }
                }
            }
//            String key = FrameworkServlet.SERVLET_CONTEXT_PREFIX + "dispatcherServlet";
//            context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext(), key);
        }
        return context;
    }

    public static void put(HttpServletRequest request) {
        put("req", request);
    }

    public static void put(HttpServletResponse response) {
        put("resp", response);
    }

    public static void put(String key, Object obj) {
        Map<String, Object> map = local.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            local.set(map);
        }
        map.put(key, obj);
    }

    public static Object get(String key) {
        Map<String, Object> map = local.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static void clean() {
        local.set(null);
    }

    public static void setCookie(String key, String val) {
        setCookie(key, val, SessionConstants.COOKIE_TIMEOUT, getCookieDomain(getRequest().getRequestURL().toString()));
    }
    public static void setCookie(String key, String val, String domain) {
        setCookie(key, val, SessionConstants.COOKIE_TIMEOUT, getCookieDomain(getRequest().getRequestURL().toString()));
    }
    /**
     * 设置COOKIE
     * @param key
     * @param val
     */
    public static void setCookie(String key, String val, Integer maxAge, String domain){
        Cookie cookie = null;
        try {
            val = val == null ? null : URLEncoder.encode(val, "utf-8");
            cookie = new Cookie(key, val);
            cookie.setDomain(domain);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            HttpServletResponse resp = WebContent.getResponse();
             resp.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("cookie写入失败");
        }
    }

    public static String getCookie(String key) {
        HttpServletRequest req = WebContent.getRequest();
        if (req.getCookies() == null) {
            return null;
        }
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(key)) {
                try {
                    return URLDecoder.decode(c.getValue(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private static String getCookieDomain(String uri) {
        try {
            return getCookieDomain(new URI(uri));
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI转换出错!");
        }
    }

    private static String getCookieDomain(URI uri) throws URISyntaxException {
        String host = uri.getHost();
        if (ip.matcher(host).find()) {
            return host;
        }
        switch (host) {
            case "127.0.0.1":
            case "localhost":
                return host;
        }
        return host.substring(host.indexOf(".")+1);
    }

    public static String getCookieVal(String cookieName){
        Cookie[] cookies = WebContent.getRequest().getCookies();
        if(cookies != null && !StringUtils.isEmpty(cookieName)){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
