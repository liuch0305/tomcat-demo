package com.lch.tomcat.http;

import com.lch.tomcat.util.RequestParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author: liuchenhui
 * @create: 2019-12-18 19:37
 **/
public class Request implements HttpServletRequest {

    HttpRequest httpRequest;

    String servletPath;

    public Request(HttpRequest request) {
        this.httpRequest = request;
    }

    @Override
    public String getMethod() {
        return httpRequest.method().name();
    }

    @Override
    public String getHeader(String name) {
        return httpRequest.headers().get(name);
    }

    @Override
    public String getRequestURL() {
        String uri = httpRequest.uri();
        if (uri.contains("?")) {
            return uri.split("\\?")[0];
        }
        return httpRequest.uri();
    }

    @Override
    public String getRequestURI() {
        return httpRequest.uri();
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        try {
            return new RequestParser(this.httpRequest).parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getParameter(String name) {
        String[] strings = getParameterMap().get(name);
        if (strings != null && strings.length != 0) {
            return strings[0];
        }
        return "";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }
}
