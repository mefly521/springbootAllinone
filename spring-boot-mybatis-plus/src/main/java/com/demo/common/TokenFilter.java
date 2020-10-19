package com.demo.common;

/**
 * @author mifei
 * @create 2020-09-10 17:21
 **/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Slf4j
@WebFilter(urlPatterns = {"/*"}, filterName = "tokenAuthorFilter")
public class TokenFilter implements Filter {

    /**
     * 头信息 访问令牌
     */
    private final String tokenName = "at";


    /**
     * 头信息 应用id
     */
    private final String applicationName = "appId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("----------------过滤器初始化------------------------");
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        setCors(request, response);
        filterChain.doFilter(servletRequest, servletResponse);

    }


    @Override
    public void destroy() {

        log.info("--------------权限过滤器销毁--------------");
    }

    /**
     * 是否需要过滤
     *
     * @param url
     * @return
     */
    private boolean isInclude(String url, String[] targetUrl) {
        for (String pattern : targetUrl) {
            if (url.indexOf(pattern) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param request :  访问请求
     * @return : java.lang.String
     * @description: 获取应用id
     * @author liyujun
     * @date 2020-07-27
     */
    private String getTerminalId(HttpServletRequest request) {
        String terminalId = request.getHeader(applicationName);
        if (terminalId == null) {
            terminalId = request.getParameter(applicationName);
        }
        return terminalId;
    }


    /**
     * @param request  :
     * @param response :
     * @return : void
     * @description: 解决跨域访问问题
     * @author liyujun
     * @date 2020-07-27
     */
    private void setCors(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header,at,appId,tid");
        //添加请求头 给前端获取文件流名称
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

    }
}
