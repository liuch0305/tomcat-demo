package com.lch.tomcat;

import com.lch.tomcat.http.HttpServletRequest;
import com.lch.tomcat.http.HttpServletResponse;
import com.lch.tomcat.http.Request;
import com.lch.tomcat.http.Response;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author: liuchenhui
 * @create: 2019-12-16 16:11
 **/
@SuppressWarnings("all")
public class TomcatServletHandler extends ChannelInboundHandlerAdapter {

    private Map<String, HttpServelt> xmlMap;

    public TomcatServletHandler(Map<String, HttpServelt> xmlMap) {
        this.xmlMap = xmlMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj == null || xmlMap == null || xmlMap.size() == 0) {
            return;
        }
        if (obj instanceof HttpRequest) {

            HttpRequest request = (HttpRequest) obj;

            HttpServletRequest nRequest = new Request(request);
            HttpServletResponse nResponse = new Response(ctx);

            String url = nRequest.getRequestURL();

            String matchUrl = "";
            if (xmlMap.containsKey(url)) {
                xmlMap.get(url).service(nRequest, nResponse);
            } else if (match(xmlMap, nRequest, matchUrl)) {
                xmlMap.get(matchUrl).service(nRequest, nResponse);
            } else {
                nResponse.sendError(404, "404-NO FOUND");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        FullHttpResponse response = new DefaultFullHttpResponse(
                // 设置http版本为1.1
                HttpVersion.HTTP_1_1,
                // 设置响应状态码
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                Unpooled.wrappedBuffer(cause.getStackTrace().toString().getBytes(CharsetUtil.UTF_8)) );
        ctx.writeAndFlush(response);
        ctx.close();
    }

    private boolean match(Map<String, HttpServelt> xmlMap, HttpServletRequest nRequest, String matchUrl) {
        for (Map.Entry<String, HttpServelt> entry : xmlMap.entrySet()) {
            String key = entry.getKey();
            String requestURI = nRequest.getRequestURI();
            if (match(key, requestURI)) {
                matchUrl = requestURI;
                return true;
            }
        }
        return false;
    }

    private static boolean match(String key, String requestURI) {
        //按 * 切割字符串
        String[] reg_split = key.split("\\*");
        int index = 0, reg_len = reg_split.length;
        //b代表匹配模式的最后一个字符是否是 '*' ,因为在split方法下最后一个 * 会被舍弃
        boolean b = key.charAt(key.length() - 1) == '*' ? true : false;
        while (requestURI.length() > 0) {
            //如果匹配到最后一段,比如这里reg的landingsuper
            if (index == reg_len) {
                if (b)//如果reg最后一位是 * ,代表通配,后面的就不用管了,返回true
                    return true;
                else  //后面没有通配符 * ,但是input长度还没有变成0 (此时input = context=%7B%22nid%22%3...),显然不匹配
                    return false;
            }
            String r = reg_split[index++];
            int indexOf = requestURI.indexOf(r);
            if (indexOf == -1)
                return false;
            //前面匹配成功的就可以不用管了,截取掉直接从新地方开始
            requestURI = requestURI.substring(indexOf + r.length());
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.printf(String.valueOf(match("/sef/*", "/sef/sdfs")));
    }
}
