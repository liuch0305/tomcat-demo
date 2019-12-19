package com.lch.tomcat;

import com.lch.tomcat.exception.ServletException;
import com.lch.tomcat.http.HttpServletRequest;
import com.lch.tomcat.http.HttpServletResponse;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: liuchenhui
 * @create: 2019-12-11 19:44
 **/
@Slf4j
public abstract class HttpServelt implements Servelt {

    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        this.init();
    }

    protected abstract void init();

    @Override
    public ServletConfig getServletConfig() {
        return config;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if ("GET".equals(req.getMethod())) {
            doGet(req, res);
        } else if ("POST".equals(req.getMethod())) {
            doPost(req, res);
        } else {
            log.info("不被支持的请求类型");
        }
    }

    @Override
    public String getServletInfo() {
        return "";
    }

    @Override
    public void destroy() {

    }

    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;

    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;
}
