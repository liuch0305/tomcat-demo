package com.lch.tomcat.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author: liuchenhui
 * @create: 2019-12-18 19:34
 **/
public class Response implements HttpServletResponse {

    ChannelHandlerContext ctx;

    FullHttpResponse response;

    public Response(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        response = new DefaultFullHttpResponse(
                // 设置http版本为1.1
                HttpVersion.HTTP_1_1,
                // 设置响应状态码
                HttpResponseStatus.OK);
        response.headers().set("Content-Type", "text/html ");
    }

    @Override
    public void setStatus(int status) {
        response.setStatus(HttpResponseStatus.valueOf(status));
    }

    @Override
    public int getStatus() {
        return response.getStatus().code();
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        response.setStatus(HttpResponseStatus.valueOf(sc));
        byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        FullHttpResponse replace = response.replace(buf);
        ctx.writeAndFlush(replace);
        ctx.close();
    }

    @Override
    public void sendError(int sc) throws IOException {
        response.setStatus(HttpResponseStatus.valueOf(sc));
        ctx.writeAndFlush(response);
        ctx.close();
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        // TODO: 2019-12-18 liuchenhui
    }

    @Override
    public void setHeader(String name, String value) {
        response.headers().set(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        response.headers().add(name, value);
    }

    @Override
    public void write(Object object) {
        byte[] bytes = object.toString().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
        FullHttpResponse replace = response.replace(buf);
        replace.headers().set("Content-length", replace.content().readableBytes());
        ctx.writeAndFlush(replace);
        ctx.close();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        // TODO: 2019-12-18 liuchenhui
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        // TODO: 2019-12-18 liuchenhui
        return null;
    }

    @Override
    public void setContentType(String type) {
        response.headers().set("Content-Type", type);
    }

    @Override
    public String getContentType() {
        return response.headers().get("Content-Type");
    }
}
