package com.lch.tomcat.bio.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author: liuchenhui
 * @create: 2019-10-28 18:38
 **/
public class BioResponse {

    private OutputStream outputStream;

    public BioResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String s) {
        try {
            // 请求有协议头，返回一一样有协议头
            StringBuilder sb = new StringBuilder();
            sb.append("HTTP/1.1 200 OK\n")
                    .append("Server: tomcat\n")
                    .append("\r\n")
                    .append(s);
            outputStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
