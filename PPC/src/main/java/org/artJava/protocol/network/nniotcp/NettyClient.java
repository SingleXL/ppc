package org.artJava.protocol.network.nniotcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.artJava.protocol.client.handlers.HeartBeatReqHandler;
import org.artJava.protocol.client.handlers.LoginAuthReqHandler;
import org.artJava.protocol.codec.MarshallingCodeCFactory;
import org.artJava.protocol.constant.NettyConstant;
import org.artJava.protocol.network.Client;
import org.artJava.protocol.pojo.Message;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author artJava
 * @date 2016年6月6日
 * @version 1.0
 */
public class NettyClient implements Client {

	private Bootstrap bootstrap;
	private EventLoopGroup workerGroup;
	private Channel channel;
	private BlockingDeque<Message> msgQ;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private String host;
	private int port;

	public NettyClient() {
		msgQ = new LinkedBlockingDeque<Message>(1000);
	}

	public void connectTo(String host, int port) throws IOException, InterruptedException {
		this.host = host;
		this.port = port;
		connect();
	}

	private void connect() throws InterruptedException {
		System.out.println("连接....");
		try {
			bootstrap = new Bootstrap();
			workerGroup = new NioEventLoopGroup();

			bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
					ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
					ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
					ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
					ch.pipeline().addLast("msgHandler", new msgHandler());
				}
			});
			// 发起异步连接操作
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
			System.out.println("连接成功...");
			channel = future.channel();
		} catch (Exception e) {
			// 所有资源释放完成之后，清空资源，再次发起重连操作
			close();
			executor.execute(new Runnable() {
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(1);
						try {
							connect();// 发起重连操作
						} finally {
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public void send(Message msg) {
		if (channel != null && channel.isOpen()) {
			channel.writeAndFlush(msg);
		}
	}

	public Message receive() {
		return msgQ.poll();
	}

	public void close() {
		try {
			if (channel != null) {
				channel.disconnect();
				channel.close();
			}
			workerGroup.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private class msgHandler extends SimpleChannelInboundHandler<Message> {
		@Override
		protected void messageReceived(ChannelHandlerContext ctx, Message msg) throws Exception {
			msgQ.put(msg);
		}
	}
	
	public boolean isConnected() {
		if (channel != null && channel.isOpen()) {
			return true;
		}else {
			return false;
		}
	}

}
