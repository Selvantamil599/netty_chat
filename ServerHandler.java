package tcpchat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Date;

public class ServerHandler extends SimpleChannelInboundHandler<HashMap> {

    private final ServerChatUtils chat;
    private String name;
    
    public ServerHandler(ServerChatUtils chat){
        this.chat = chat;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(chat.addUser(ctx.channel().id().asLongText(),ctx.channel()));
        this.name = ctx.channel().id().asLongText();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        chat.deleteUser(this.name);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HashMap req) {
        if(req.containsKey("isShowOnline")){
            ctx.writeAndFlush(chat.getActiveUsers());
        }else if(req.containsKey("name")){
            HashMap data = chat.reNameUser(this.name, (String)req.get("name"), ctx.channel());
            ctx.writeAndFlush(data);
            if(((String)data.get("status")).equalsIgnoreCase("success")){
                this.name = (String)req.get("name");
            }
        }else if(req.containsKey("message")){
            ctx.writeAndFlush(chat.sendMessage(this.name,(String)req.get("to"), (String)req.get("message"), (Date)req.get("time")));
        }else{
            HashMap res = new HashMap();
            res.put("status","failed");
            res.put("message","Operation not supported");
            ctx.writeAndFlush(res);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}