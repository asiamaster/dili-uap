package com.dili.uap.filter;

import org.apache.catalina.connector.RequestFacade;

import javax.servlet.*;
import java.io.IOException;

/**
 *
 * Created by asiam on 2017/4/20 0020.
 */
//@Component("myFilter") //Component方式的myFilter配置的映射是/*
//@WebFilter(filterName="logFilter",urlPatterns="/*")
public class LogFilter implements Filter {
    /**
     *
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        System.out.println("destroy...");
    }
    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws IOException
     * @throws ServletException
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException,
            ServletException {
        if (arg0 instanceof RequestFacade) {
            String uri = ((RequestFacade) arg0).getRequestURI();
            if(uri.endsWith(".action")) {
                System.out.println("doFilter:" + uri);
            }
        }
        arg2.doFilter(arg0, arg1);
    }
    /**
     * @param arg0
     * @throws ServletException
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("filter init...");
    }
}
