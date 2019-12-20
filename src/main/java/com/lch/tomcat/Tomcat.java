package com.lch.tomcat;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: liuchenhui
 * @create: 2019-12-16 15:53
 **/
@Slf4j
public class Tomcat {
    public static final String SERVLET_PORT = "port";
    private int port;
    private Properties properties = new Properties();
    private Map<String, HttpServelt> xmlMap = new HashMap<>();

    public Tomcat() throws Exception {
        init();
    }

    private void init() throws Exception {
        String path = this.getClass().getResource("/").getPath();
        // TODO: 2019-12-16 liuchenhui 如何替换web.properties文件配置
        FileInputStream fileInputStream = new FileInputStream(path + "web.properties");
        properties.load(fileInputStream);

        Set<Object> objects = properties.keySet();
        for (Object k : objects) {
            String kStr = (String) k;
            if (SERVLET_PORT.equals(k)) {
                try {
                    this.port = Integer.parseInt(properties.getProperty(kStr));
                    continue;
                } catch (NumberFormatException e) {
                    log.error("port is not a number", e);
                }
            }
            if (!kStr.endsWith("url")) {
                continue;
            }
            String url = properties.getProperty(kStr);
            String serverName = kStr.replace(".url", "");
            String serverClass = properties.getProperty(serverName + ".servlet");
            String initParam = properties.getProperty(serverName + ".initParam");
            HttpServelt httpServelt = (HttpServelt) Class.forName(serverClass).newInstance();
            ServletConfig servletConfig = new ServletConfig() {
                @Override
                public String getServletName() {
                    return serverName;
                }

                @Override
                public String getInitParameter() {
                    return initParam;
                }
            };
            httpServelt.init(servletConfig);
            xmlMap.put(url, httpServelt);
        }
    }

    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpResponseEncoder())
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new TomcatServletHandler(xmlMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture sync = serverBootstrap.bind(port).sync();
            System.out.println("Netty Tomcat已启动，监听端口" + port);
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws Exception {
        new Tomcat().start();
    }
}
