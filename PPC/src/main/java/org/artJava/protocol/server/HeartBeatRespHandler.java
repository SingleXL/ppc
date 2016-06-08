/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.artJava.protocol.server;

import org.artJava.protocol.constant.MessageType;
import org.artJava.protocol.pojo.Header;
import org.artJava.protocol.pojo.Message;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author artJava
 * @date 2016年6月6日
 * @version 1.0
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		Message message = (Message) msg;
		// 返回心跳应答消息
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
			System.out.println("Receive client heart beat message : ---> " + message);
			Message heartBeat = buildHeatBeat();
			System.out.println("Send heart beat response message to client : ---> " + heartBeat);
			ctx.writeAndFlush(heartBeat);
		} else
			ctx.fireChannelRead(msg);
	}

	private Message buildHeatBeat() {
		Message message = new Message();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}

}
