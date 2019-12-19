package com.lch.tomcat.example.netty;


import com.lch.tomcat.example.netty.http.NettyRequest;
import com.lch.tomcat.example.netty.http.NettyResponse;
import com.lch.tomcat.example.netty.http.NettyServlet;

import java.util.Map;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author: liuchenhui
 * @create: 2019-10-14 13:01
 **/
public class ServletHandler extends ChannelInboundHandlerAdapter {


    private Map<String, NettyServlet> xmlMap;

    public ServletHandler(Map<String, NettyServlet> xmlMap) {
        this.xmlMap = xmlMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) {
            return;
        }
        if (msg instanceof HttpRequest) {

            HttpRequest request = (HttpRequest) msg;

            NettyRequest nRequest = new NettyRequest(request);
            NettyResponse nResponse = new NettyResponse(ctx);

            String url = nRequest.getUrl();

            if (xmlMap.containsKey(url)) {
                xmlMap.get(url).service(nRequest, nResponse);
            } else {
                try {
                    // 设置 http协议及请求头信息
                    FullHttpResponse response = new DefaultFullHttpResponse(
                            // 设置http版本为1.1
                            HttpVersion.HTTP_1_1,
                            // 设置响应状态码
                            HttpResponseStatus.NOT_FOUND,
                            // 将输出值写出 编码为UTF-8
                            Unpooled.wrappedBuffer("404 - Not Found".getBytes("UTF-8")));

                    response.headers().set("Content-Type", "text/html;");
                    ctx.write(response);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ctx.flush();
                    ctx.close();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
