package org.artJava.protocol.app;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.artJava.protocol.network.Server;
import org.artJava.protocol.network.nniotcp.NettyServer;
import org.artJava.protocol.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class NodeMaster {
	private static final Logger LOGGER = LoggerFactory.getLogger(NodeMaster.class);

	private final String bindIP;
	private final int bindPort;
	private Server server;
	private Map<String, Channel> channelMap;

	// threads
	private Thread msgListener;

	// locks
	private Lock mainLock;

	public NodeMaster(String host, int port) {
		bindIP = host;
		bindPort = port;
		server = new NettyServer();
		mainLock = new ReentrantLock();
		initMsgListener();
	}

	public void start() throws Exception {
		LOGGER.info("Starting node master... ");
		buildCaches();
		server.bind(bindIP, bindPort);
		msgListener.start();
	}

	public void stop() {
		LOGGER.info("Stopping node master... ");
		try {
			msgListener.interrupt();
			msgListener.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		server.close();
		freeCaches();
		LOGGER.info("Node master stopped. ");
	}

	private void buildCaches() {
		channelMap = new HashMap<String, Channel>();
	}

	private void freeCaches() {
		mainLock.lock();
		try {
			channelMap = null;
		} finally {
			mainLock.unlock();
		}
	}

	private void initMsgListener() {
		msgListener = new Thread(new Runnable() {
			public void run() {
				while (!Thread.interrupted()) {
					try {
						handle(server.receive());
					} catch (Exception e) {
						Thread.currentThread().interrupt();
					} 
				}
			}
		}, "NodeMaster - msg listener");
	}

	private void handle(Message msg) {
		System.out.println(msg);
	}

}