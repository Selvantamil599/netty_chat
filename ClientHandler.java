package tcpchat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Date;

@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<HashMap> {

    @Override
    public void channelActive(ChannelHandlerContext ctx){
         System.out.println("You are connected now! ;)");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
         System.out.println("You are disconnected now! :(");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HashMap response) {
        System.out.println(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}