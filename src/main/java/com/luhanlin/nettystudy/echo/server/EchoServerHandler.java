package com.luhanlin.nettystudy.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 类详细描述：服务自定义的channel handler
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/12/5 9:44 AM
 */
// 表示一个ChannelHandler 可以被多个channel安全的共享
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("server received : " + buf.toString(CharsetUtil.UTF_8));
        // 将接受到的东西发送给发送者而不冲刷出站？
        ctx.write(buf);
    }

    // 此方法进行确认调用的是此批量数据中的最后一条
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // todo 将未决的消息冲刷出站，并且关闭channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
