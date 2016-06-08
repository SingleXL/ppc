package org.artJava.protocol.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.artJava.protocol.codec.MarshallingCodeCFactory;
import org.artJava.protocol.constant.PPCConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author artJava
 * @date 2016年6月6日
 * @version 1.0
 */
public class PPCClient {

    private ScheduledExecutorService executor = Executors
	    .newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {

	// 配置客户端NIO线程组

	try {
	    Bootstrap b = new Bootstrap();
	    b.group(group).channel(NioSocketChannel.class)
		    .option(ChannelOption.TCP_NODELAY, true)
		    .handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch)
					throws Exception {
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
					ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
				    ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
				    ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
				}
		    });
	    // 发起异步连接操作
	    ChannelFuture future = b.connect(new InetSocketAddress(host, port), 
	    		new InetSocketAddress(PPCConstant.LOCALIP,PPCConstant.LOCAL_PORT)).sync();
	    
	    future.channel().closeFuture().sync();
	} finally {
	    // 所有资源释放完成之后，清空资源，再次发起重连操作
	    executor.execute(new Runnable() {
			public void run() {
			    try {
					TimeUnit.SECONDS.sleep(1);
					try {
					    connect(PPCConstant.PORT, PPCConstant.REMOTEIP);// 发起重连操作
					} catch (Exception e) {
					    e.printStackTrace();
					}
			    } catch (InterruptedException e) {
			    	e.printStackTrace();
			    }
			}
		    });
		}
    }

    public static void main(String[] args) throws Exception {
    	new PPCClient().connect(PPCConstant.PORT, PPCConstant.REMOTEIP);
    }

}
