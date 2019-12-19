package com.lch.tomcat.http;

import com.lch.tomcat.ServletRequest;

/**
 * @author: liuchenhui
 * @create: 2019-12-11 19:37
 **/
public interface HttpServletRequest extends ServletRequest {

    String getMethod();

    public String getHeader(String name);

    public String getRequestURL();

    public String getRequestURI();

}
