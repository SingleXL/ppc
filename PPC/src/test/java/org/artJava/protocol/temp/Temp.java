package org.artJava.protocol.temp;

import java.util.concurrent.SynchronousQueue;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Temp {

	public static void main(String[] args) {

		try {

			System.out.println(123);
			throw new IllegalStateException();
		}

		catch (Exception e) {
			System.out.println(1234);
			
		} finally {
			System.out.println(129);
		}

	}

}
