package tcpchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.serialization.ClassResolvers;

public class Client {

    private final String host;
    private final int port;

    private EventLoopGroup group;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Channel connect() {
        Channel channel = null;
        group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap(); 
            client.group(group); 
            client.channel(NioSocketChannel.class);
            client.handler(new ChannelInitializer<SocketChannel>() { 
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)), new ObjectEncoder(), new ClientHandler()); 
                }
            });

            channel = client.connect(this.host, this.port).sync().channel(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    public void shutdownGracefully(){
        group.shutdownGracefully();
    }
}
