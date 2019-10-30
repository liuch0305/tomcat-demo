package com.lch.tomcat.bio;

import com.lch.tomcat.bio.http.BioRequest;
import com.lch.tomcat.bio.http.BioResponse;
import com.lch.tomcat.bio.http.BioServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private Properties properties = new Properties();
    private Map<String, BioServlet> xmlMap = new HashMap<>();

    public BioTomcat(int port) throws Exception {
        this.port = port;
        init();
    }

    // 解析web.xml（这里采用bio-web.properties代替）
    void init() throws Exception {
        String path = this.getClass().getResource("/").getPath();
        FileInputStream fileInputStream = new FileInputStream(path + "bio-web.properties");
        properties.load(fileInputStream);

        Set<Object> objects = properties.keySet();
        for (Object k : objects) {
            String kStr = (String) k;
            if (!kStr.endsWith("url")) {
                continue;
            }
            String serverName = kStr.replace(".url", "");
            String url = properties.getProperty(kStr);
            String serverClass = properties.getProperty(serverName + ".servlet");
            xmlMap.put(url, (BioServlet) Class.forName(serverClass).newInstance());
        }
    }

    /**
     * 开启tomcat服务
     */
    void start() throws IOException {
        /**
         * 1、开启serverSocket，监听post端口
         * 2、有数据请求先创建request、response
         * 3、处理resquest、response
         */

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("开始监听" + port + "端口");
        while (true) {
            Socket socket = serverSocket.accept();

            executorService.submit(() -> {
                try {
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    BioRequest request = new BioRequest(inputStream);
                    BioResponse response = new BioResponse(outputStream);

                    process(request, response);

                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    // 处理resquest、response
    void process(BioRequest request, BioResponse response) throws IOException {
        String url = request.getUrl();
        if (xmlMap.containsKey(url)) {
            BioServlet bioServlet = xmlMap.get(url);
            bioServlet.service(request, response);
        } else {
            StringBuilder str404 = new StringBuilder();
            str404.append("HTTP/1.1 404 OK\n")
                    .append("Server: tomcat\n")
                    .append("\r\n")
                    .append("404");
            response.getOutputStream().write(str404.toString().getBytes());
        }
    }

    public static void main(String[] args) throws Exception {
        new BioTomcat(8080).start();
    }
}
