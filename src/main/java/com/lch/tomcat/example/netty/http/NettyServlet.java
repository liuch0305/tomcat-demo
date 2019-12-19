package com.lch.tomcat.example.netty.http;

/**
 * @author: liuchenhui
 * @create: 2019-10-28 18:40
 **/
public abstract class NettyServlet {
    public void service(NettyRequest request, NettyResponse response) {
        if ("GET".equals(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    protected abstract void doPost(NettyRequest request, NettyResponse response);

    protected abstract void doGet(NettyRequest request, NettyResponse response);
}
