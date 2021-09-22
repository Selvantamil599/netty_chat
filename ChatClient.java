package tcpchat;

import io.netty.channel.Channel;
import tcpchat.Client;
import io.netty.buffer.Unpooled;

import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;

public class ChatClient {
    public static void main(String args[]) throws Exception{
        Client client = new Client("localhost", 9000);
        try{
            Channel channel = client.connect();
            Scanner in = new Scanner(System.in);  
            while(true)
            {
                HashMap req = new HashMap();
                String text[] = in.nextLine().split("-");
                if(text[0].equalsIgnoreCase("online")){
                    req.put("isShowOnline", true);
                }else if(text[0].equalsIgnoreCase("rename")){
                    req.put("name",text[1]);
                }else if(text[0].equalsIgnoreCase("msg")){
                    req.put("to",text[1]);
                    req.put("message",text[2]);
                    req.put("time", new Date());
                }else if(text[0].equalsIgnoreCase("exit")){
                    break;
                }else{
                    System.out.println("Illegeal Operation");
                }
                channel.writeAndFlush(req).sync();
            }
            channel.close().sync();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            client.shutdownGracefully();
        }
    }
}
