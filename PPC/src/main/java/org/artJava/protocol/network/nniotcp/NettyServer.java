package org.artJava.protocol.network.nniotcp;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.artJava.protocol.codec.MarshallingCodeCFactory;
import org.artJava.protocol.constant.MessageType;
import org.artJava.protocol.constant.NettyConstant;
import org.artJava.protocol.network.Server;
import org.artJava.protocol.pojo.Header;
import org.artJava.protocol.pojo.Message;
import org.artJava.protocol.server.handlers.HeartBeatRespHandler;
import org.artJava.protocol.server.handlers.LoginAuthRespHandler;
import org.artJava.protocol.server.singleton.MsgChannels;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author artJava
 * @date 2016年6月6日
 * @version 1.0
 */
public class NettyServer implements Server {

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private BlockingQueue<Message> msgQ;
	private Map<String, Channel> channelMap;

	public NettyServer() {
		bossGroup = null;
		workerGroup = null;
		msgQ = new LinkedBlockingQueue<Message>();
		channelMap = new HashMap<String, Channel>();
	}

	public void bind(String host, int port) throws Exception {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws IOException {
				ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
				ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
				ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(60));
				ch.pipeline().addLast(new LoginAuthRespHandler());
				ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
				ch.pipeline().addLast("msgHandler", new MsgHandler());
			}
		});

		// 绑定端口，同步等待成功
		b.bind(host, port).sync();
		System.out.println("Netty server start ok : " + (host + " : " + port));
	}

	public Message receive() {
		return msgQ.poll();
	}

	public void close() {
		if (workerGroup != null) {
			workerGroup.shutdownGracefully();
		}
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
	}

	@SuppressWarnings("rawtypes")
	private class MsgHandler extends SimpleChannelInboundHandler {
		
		@Override
		protected void messageReceived(ChannelHandlerContext ctx, Object _msg) throws Exception {
			if (_msg != null) {
				Message msg = (Message) _msg;
				msgQ.put(msg);
				channelMap.put(msg.getHeader().getExecutorUID(), ctx.channel());
				MsgChannels.channelGroup.add(ctx.channel());
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			super.exceptionCaught(ctx, cause);
			System.out.println("error....");
		}

	}

	public void send(String executorUID, Message msg) {
		channelMap.get(executorUID).writeAndFlush(msg);
	}


}
