package org.artJava.protocol.app;

import org.artJava.protocol.constant.NettyConstant;
import org.artJava.protocol.network.Client;
import org.artJava.protocol.network.nniotcp.NettyClient;

public class NodeExecutor {

	public static void main(String[] args) throws Exception{
		
		Client client = new NettyClient();
		client.connectTo(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT);
		
	}
	
}
