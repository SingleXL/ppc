package org.artJava.protocol.app;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.artJava.protocol.network.Server;
import org.artJava.protocol.network.nniotcp.NettyServer;
import org.artJava.protocol.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMaster {
	private static final Logger LOGGER = LoggerFactory.getLogger(NodeMaster.class);

	private final String bindIP;
	private final int bindPort;
	private Server server;

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

	private void freeCaches() {
		mainLock.lock();
		try {
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
		if (msg!=null) {
			System.out.println(msg);
		}
	}
	
	public static void main(String[] args) throws Exception{
		
		NodeMaster nm = new NodeMaster("127.0.0.1",8888);
		nm.start();
	}

}