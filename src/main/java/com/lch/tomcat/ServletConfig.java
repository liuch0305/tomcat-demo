package com.lch.tomcat;

/**
 * @author: liuchenhui
 * @create: 2019-12-11 19:41
 **/
public interface ServletConfig {

    public String getServletName();

    public String getInitParameter(String name);
}
