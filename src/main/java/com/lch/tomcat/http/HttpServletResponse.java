package com.lch.tomcat.http;

import com.lch.tomcat.ServletResponse;

import java.io.IOException;

/**
 * @author: liuchenhui
 * @create: 2019-12-11 19:38
 **/
public interface HttpServletResponse extends ServletResponse {

    void setStatus(int status);

    public int getStatus();

    public void sendError(int sc, String msg) throws IOException;

    public void sendError(int sc) throws IOException;

    public void sendRedirect(String location) throws IOException;

    public void setHeader(String name, String value);

    public void addHeader(String name, String value);

    public void write(Object e);
}
