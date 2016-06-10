package org.artJava.protocol.network;

import org.artJava.protocol.pojo.Message;

public interface Server {

    void bind(String host, int port) throws Exception;

    Message receive() ;

    void close();
    
    void send(String executorUID,Message msg);
    
}
