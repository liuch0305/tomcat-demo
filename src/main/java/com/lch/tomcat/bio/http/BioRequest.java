package com.lch.tomcat.bio.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: liuchenhui
 * @create: 2019-10-28 18:38
 **/
public class BioRequest {

    private String url;
    private String method;

    public BioRequest(InputStream inputStream) {
        try {
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            if (read > 0) {
                String httpStr = new String(bytes, "UTF-8");
                // 拿到第一行数据 GET / HTTP/1.1, 只是做演示，不做过多的判断
                String fisrtLine = httpStr.split("\\n")[0];
                String[] fisrtLineArr = fisrtLine.split(" ");

                url = fisrtLineArr[0];
                method = fisrtLineArr[1];

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
