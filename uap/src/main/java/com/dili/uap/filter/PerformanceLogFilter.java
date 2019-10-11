package com.dili.uap.filter;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@WebFilter(filterName="logFilter",urlPatterns="/*")
public class PerformanceLogFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(PerformanceLogFilter.class);
    private int timeSpentThresholdToLog = 500;
    public static final String VIEW_PERFORMANCE_URL = "/performance.html";
    private static final Map<String, PerformanceLogFilter.RequestHandleInfo> MAP = new ConcurrentHashMap();
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String requestURI = httpServletRequest.getRequestURI();
        if(requestURI.equals(VIEW_PERFORMANCE_URL)) {
            show(response);
            return;
        }
        long timeSpent = next(request, response, chain);
        saveTimeSpent(request, timeSpent);
    }

    /**
     * 执行业务逻辑
     * @param request
     * @param response
     * @param chain
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private long next(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long timeBefore1 = System.currentTimeMillis();
        chain.doFilter(request, response);
        long timeAfter1 = System.currentTimeMillis();
        return timeAfter1 - timeBefore1;
    }

    /**
     * 保存执行时间
     * @param request
     * @param timeSpent
     */
    private void saveTimeSpent(ServletRequest request, long timeSpent) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String requestURI = httpServletRequest.getRequestURI();
        if(timeSpent >= (long)this.timeSpentThresholdToLog) {
            LOG.info("" + timeSpent + " millions to process request:[" + requestURI + " Param Map:" + JSONObject.toJSONString(request.getParameterMap()));
            String uri;
            if(requestURI.contains("?")) {
                uri = requestURI.substring(0, requestURI.indexOf("?"));
            } else {
                uri = requestURI;
            }

            RequestHandleInfo requestHandleInfo;
            if(MAP.containsKey(uri)) {
                requestHandleInfo = (RequestHandleInfo)MAP.get(uri);
                int lastAccessCount = requestHandleInfo.accessCount;
                float lastAverageCostSeconds = requestHandleInfo.averageCostSeconds;
                requestHandleInfo.accessCount = lastAccessCount + 1;
                requestHandleInfo.averageCostSeconds = (lastAverageCostSeconds * (float)lastAccessCount + (float)timeSpent) / (float)(lastAccessCount + 1);
                requestHandleInfo.lastAccessTime = new Date();
            } else {
                requestHandleInfo = new RequestHandleInfo();
                requestHandleInfo.uri = uri;
                requestHandleInfo.accessCount = 1;
                requestHandleInfo.averageCostSeconds = (float)timeSpent;
                requestHandleInfo.lastAccessTime = new Date();
                MAP.put(uri, requestHandleInfo);
            }
        }
    }

    /**
     * 显示性能统计
     * @param response
     * @throws IOException
     */
    private void show(ServletResponse response) throws IOException {
        StringBuilder timeBefore = new StringBuilder();
        timeBefore.append("<!DOCTYPE html>");
        timeBefore.append("<html>");
        timeBefore.append("<head lang=\"en\">");
        timeBefore.append("    <meta charset=\"UTF-8\">");
        timeBefore.append("    <title>性能统计</title>");
        timeBefore.append("</head>");
        timeBefore.append("<body>");
        timeBefore.append("<div style=\" margin:0 auto; width:1023px;margin-top: 50px;\">");
        timeBefore.append("    <table style=\"text-align: center;border-collapse: collapse; border: 1px #999999 solid;line-height:28px;\" border=\"1\">");
        timeBefore.append("        <tr>");
        timeBefore.append("            <td>URI</td>");
        timeBefore.append("            <td width=\"130\">超过0.5秒次数</td>");
        timeBefore.append("            <td width=\"130\">平均耗时(毫秒)</td>");
        timeBefore.append("            <td width=\"190\">上次访问时间</td>");
        timeBefore.append("        </tr>");
        List<RequestHandleInfo> infoList = new ArrayList(MAP.values());
        Collections.sort(infoList, new Comparator<PerformanceLogFilter.RequestHandleInfo>() {
            public int compare(PerformanceLogFilter.RequestHandleInfo r1, PerformanceLogFilter.RequestHandleInfo r2) {
                return r1.averageCostSeconds < r2.averageCostSeconds?1:-1;
            }
        });
        Iterator timeAfter = infoList.iterator();

        while(timeAfter.hasNext()) {
            PerformanceLogFilter.RequestHandleInfo info = (PerformanceLogFilter.RequestHandleInfo)timeAfter.next();
            timeBefore.append("        <tr>");
            timeBefore.append("            <td>").append(info.uri).append("</td>");
            timeBefore.append("            <td>").append(info.accessCount).append("</td>");
            timeBefore.append("            <td>").append(info.averageCostSeconds).append("</td>");
            timeBefore.append("            <td>").append(format.format(info.lastAccessTime)).append("</td>");
            timeBefore.append("        </tr>");
        }

        timeBefore.append("    </table>");
        timeBefore.append("</div>");
        timeBefore.append("</body>");
        timeBefore.append("</html>");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(timeBefore.toString());
    }

    public void setTimeSpentThresholdToLog(int timeSpentThresholdToLog) {
        this.timeSpentThresholdToLog = timeSpentThresholdToLog;
    }

    public void destroy() {
    }

    private class RequestHandleInfo {
        String uri;
        int accessCount;
        float averageCostSeconds;
        Date lastAccessTime;

        private RequestHandleInfo() {
        }
    }
}

