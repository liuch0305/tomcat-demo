package com.lch.tomcat.netty.http;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author: liuchenhui
 * @create: 2019-10-28 18:38
 **/
public class NettyRequest {

    private String url;
    private String mothed;

    public NettyRequest(HttpRequest request) {
        this.url = request.getUri();
        this.mothed = request.getMethod().name();
    }


    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return mothed;
    }
}
