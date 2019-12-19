package com.lch.tomcat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author: liuchenhui
 * @create: 2019-12-11 19:37
 **/
public interface ServletRequest {

    public Map<String, String[]> getParameterMap();

    public String getParameter(String name);

    public InputStream getInputStream() throws IOException;

    public int getContentLength();

    public long getContentLengthLong();

    public String getContentType();
}
