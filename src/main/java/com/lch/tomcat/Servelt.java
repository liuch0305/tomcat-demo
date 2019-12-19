package com.lch.tomcat;

import com.lch.tomcat.exception.ServletException;
import com.lch.tomcat.http.HttpServletRequest;
import com.lch.tomcat.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author: liuchenhui
 * @create: 2019-12-11 19:38
 **/
public interface Servelt {

    public void init(ServletConfig config) throws ServletException;

    public ServletConfig getServletConfig();

    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException;

    public String getServletInfo();

    public void destroy();
}
