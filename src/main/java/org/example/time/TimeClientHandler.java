package org.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf firstMsg;

    public TimeClientHandler() {
        byte[] bytes = "query time order".getBytes();
        firstMsg = Unpooled.buffer(bytes.length);
        firstMsg.writeBytes(bytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       ctx.writeAndFlush(firstMsg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] resp = new byte[buf.readableBytes()];
        buf.readBytes(resp);
        System.out.println("Now is " + new String(resp));

        Thread.sleep(2000);
        byte[] req = "query time order".getBytes();
        ByteBuf sendMsg = Unpooled.buffer(req.length);
        sendMsg.writeBytes(req);
        ctx.writeAndFlush(sendMsg);

    }

    // 不发送消息直接从服务器读取一次
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        try {
//            long currntTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currntTimeMillis));
//            ctx.close();
//        } finally {
//            buf.release();
//        }
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
       ctx.close();
    }
}
