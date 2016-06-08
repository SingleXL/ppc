package org.artJava.protocol.server;

import java.io.IOException;

import org.artJava.protocol.codec.MarshallingCodeCFactory;
import org.artJava.protocol.constant.PPCConstant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
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
public class PPCServer {

	public void bind() throws Exception {
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws IOException {
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
					ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
					ch.pipeline().addLast(new LoginAuthRespHandler());
					ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
				}
			});

		// 绑定端口，同步等待成功
		ChannelFuture future = b.bind(PPCConstant.REMOTEIP, PPCConstant.PORT).sync();
		System.out.println("Netty server start ok : " + (PPCConstant.REMOTEIP + " : " + PPCConstant.PORT));
		// 等待服务端监听端口关闭
		future.channel().closeFuture().sync();
		
	}

	public static void main(String[] args) throws Exception {
		new PPCServer().bind();
	}
}
