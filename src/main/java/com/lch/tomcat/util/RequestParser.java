package com.lch.tomcat.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

/**
 * @author: liuchenhui
 * @create: 2019-12-19 10:19
 **/
public class RequestParser {
    private HttpRequest fullReq;

    /**
     * 构造一个解析器
     */
    public RequestParser(HttpRequest req) {
        this.fullReq = req;
    }

    /**
     * 解析请求参数
     *
     * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
     */
    public Map<String, String[]> parse() throws Exception, IOException {
        HttpMethod method = fullReq.method();

        Map<String, String[]> parmMap = new HashMap<>();

        if (HttpMethod.GET == method) {
            // 是GET请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
            decoder.parameters().entrySet().forEach(entry -> {
                // entry.getValue()是一个List, 只取第一个元素
                String[] a = new String[]{};
                parmMap.put(entry.getKey(), entry.getValue().toArray(a));
            });
        } else if (HttpMethod.POST == method) {
            // 是POST请求
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
            decoder.offer((HttpContent) fullReq);

            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

            for (InterfaceHttpData parm : parmList) {
                Attribute data = (Attribute) parm;
                String name = data.getName();
                String value = data.getValue();
                if (parmMap.containsKey(name)) {
                    String[] strings = parmMap.get(name);
                    List<String> list = Arrays.asList(strings);
                    list.add(value);
                    parmMap.put(name, list.toArray(new String[]{}));
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(value);
                    parmMap.put(name, list.toArray(new String[]{}));
                }
            }

        } else {
            // 不支持其它方法
            throw new Exception(""); // 这是个自定义的异常, 可删掉这一行
        }

        return parmMap;
    }
}
