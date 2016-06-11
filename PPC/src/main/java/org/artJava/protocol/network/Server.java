package org.artJava.protocol.network;

import java.util.Map;

import org.artJava.protocol.pojo.Message;

import io.netty.channel.Channel;

public interface Server {

    void bind(String host, int port) throws Exception;

    Message receive() ;

    void close();
    
    void send(String executorUID,Message msg);
    
}
