package com.demo.netty.sendstring;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 * @author eli
 * @date 2017/9/12 11:12
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    // 客户端连接服务器后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接服务器，开始发送数据……");
        byte[] req = "send data 1 ......".getBytes();
        ByteBuf firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        ctx.writeAndFlush(firstMessage);
    }



    // • 从服务器接收到数据后调用
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("client 读取server数据..");
        ByteBuf buf = (ByteBuf) byteBuf;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String (req,"UTF-8");
        System.out.println("服务端数据为 :" + body);

    }

    // • 发生异常时被调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client exceptionCaught..");
        ctx.close();
    }
}
