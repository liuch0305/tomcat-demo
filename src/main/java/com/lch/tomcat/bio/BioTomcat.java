package com.lch.tomcat.bio;

import com.lch.tomcat.bio.http.BioRequest;
import com.lch.tomcat.bio.http.BioResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * bio版Tomcat入口
 *
 * @author: liuchenhui
 * @create: 2019-10-28 18:37
 **/
@SuppressWarnings("all")
public class BioTomcat {

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    private int port;

    public BioTomcat(int port) {
        this.port = port;
        init();
    }

    // 解析web.xml（这里采用bio-web.properties代替）
    void init() {

    }

    /**
     * 开启tomcat服务
     */
    void start() {
        /**
         * 1、开启serverSocket，监听post端口
         * 2、有数据请求先创建request、response
         * 3、处理resquest、response
         */

    }

    // 处理resquest、response
    void process(BioRequest request, BioResponse response) {

    }

    public static void main(String[] args) {
        new BioTomcat(8080).start();
    }
}
