package com.demo.netty.sendstring;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端启动类
 * -配置服务器功能，如线程、端口
 * -实现服务器处理程序，它包含业务逻辑，决定当有一个请求连接或接收数据时该做什么
 * @author eli
 * @date 2017/9/12 11:12
 */
public class EchoServer {
    private final int port;
    public EchoServer (int port){
        this.port=port;
    }

    /**
     * 思路整理:
     * 1.建立server端引导类ServerBootstrap的实例.
     * @throws Exception
     */
    public void start() throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress("localhost", port)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new EchoServerHandler());
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        System.out.println("开始监听，端口为：" + channelFuture.channel().localAddress());
        channelFuture.channel().closeFuture().sync();
        eventLoopGroup.shutdownGracefully().sync();
    }
    public static void main(String[] args) throws Exception {
        new EchoServer(20000).start();
    }

}


/**
 * ServerBootstrap:创建一个服务端Channel和接受传入连接的帮助类,.
 *    用法;一个父channel应该是一个接受传入连接的channel. 它是通过该启动器的ChannelFactory对象通过 bind() 和 bind(SocketAddress)创建的,一旦绑定成功，该父channel开始接受传入的连接，而且接受了的连接则成为该父channel的子channel.
 *

 * EventLoopGroup:
 * Group：群组，Loop：循环，Event：事件
 * EventLoopGroup就是用来管理调度他们的，注册Channel，管理他们的生命周期
 *
 * ChannelFuture:一个通道的异步 I/O操作结果.
 *
 */
