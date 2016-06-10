package org.artJava.protocol.client.handlers;

import org.artJava.protocol.constant.MessageType;
import org.artJava.protocol.pojo.Header;
import org.artJava.protocol.pojo.Message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

public class LoginAuthReqHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLoginReq());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message message = (Message) msg;
		// 如果是握手应答消息，需要判断是否认证成功
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			byte loginResult = (Byte) message.getBody();
			if (loginResult != (byte) 0) {
				// 握手失败，关闭连接
				ctx.close();
			} else {
				System.out.println("Login is ok : " + message);
				ctx.fireChannelRead(msg);
			}
		} else
			ctx.fireChannelRead(msg);
	}

	private Message buildLoginReq() {
		Message message = new Message();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.fireExceptionCaught(cause);
	}
}
