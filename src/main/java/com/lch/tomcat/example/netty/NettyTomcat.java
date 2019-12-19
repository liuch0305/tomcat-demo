package com.lch.tomcat.example.netty;

import com.lch.tomcat.example.netty.http.NettyServlet;

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

/**
 * netty版Tomcat入口,与bio版基本一致，netty本身就支持http协议，省去了解析http协议的步骤
 *
 * @author: liuchenhui
 * @create: 2019-10-28 18:37
 **/
@SuppressWarnings("all")
public class NettyTomcat {

    private int port;
    private Properties properties = new Properties();
    private Map<String, NettyServlet> xmlMap = new HashMap<>();

    public NettyTomcat(int port) throws Exception {
        this.port = port;
        init();
    }

    void init() throws Exception {
        String path = this.getClass().getResource("/").getPath();
        FileInputStream fileInputStream = new FileInputStream(path + "netty-web.properties");
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
            xmlMap.put(url, (NettyServlet) Class.forName(serverClass).newInstance());
        }
    }

    void start(){
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
                                    .addLast(new ServletHandler(xmlMap));
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
        new NettyTomcat(8080).start();
    }
}
