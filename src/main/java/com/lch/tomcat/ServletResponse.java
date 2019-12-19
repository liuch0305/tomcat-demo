package com.lch.tomcat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author: liuchenhui
 * @create: 2019-12-11 19:38
 **/
public interface ServletResponse {

    public OutputStream getOutputStream() throws IOException;

    public PrintWriter getWriter() throws IOException;

    public void setContentType(String type);

    public String getContentType();
}
