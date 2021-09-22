package tcpchat;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Date;
import java.util.Enumeration;

import io.netty.channel.Channel;

public class ServerChatUtils {
    private Hashtable<String, Channel> connections;

    ServerChatUtils(){
        connections = new Hashtable<String, Channel>();
    }

    public HashMap addUser(String name, Channel ch){
        HashMap result = new HashMap();
        if(connections.containsKey(name)){
            result.put("status","failed");
            result.put("error_message", "Username already exists.");
        } else{
            connections.put(name, ch);
            result.put("status", "success");
            result.put("name", name);
        }
        return result;
    }

    public HashMap reNameUser(String from, String to, Channel ch){
        HashMap result = new HashMap();
        deleteUser(from);
        return addUser(to, ch);
    }

    public void deleteUser(String name){
        connections.remove(name);
    }

    public HashMap getActiveUsers(){
        HashMap users = new HashMap();
        Enumeration names = connections.keys();
        Integer count = 0;
        while(names.hasMoreElements())
            users.put((++count).toString(),names.nextElement());
        return users;
    }

    public HashMap sendMessage(String from, String to, String message, Date time){
        HashMap result = new HashMap();
        HashMap send;
        if(connections.containsKey(to))
        {
            send = new HashMap();
            send.put("from", from);
            send.put("to", to);
            send.put("message", message);
            send.put("time", time);
            Channel target = (Channel) connections.get(to);
            if(target.writeAndFlush(send).isSuccess()){
                result.put("status", "success");
            }else{
                result.put("status", "failed");
                result.put("error_message", "Message sent failed");
            }
        }
        else
        {
            result.put("status", "failed");
            result.put("message", "Either user not found or Inactive");
        }
        return result;
    }
}
