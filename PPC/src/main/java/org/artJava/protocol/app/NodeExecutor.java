package org.artJava.protocol.app;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.artJava.protocol.constant.MessageType;
import org.artJava.protocol.network.Client;
import org.artJava.protocol.network.nniotcp.NettyClient;
import org.artJava.protocol.pojo.Header;
import org.artJava.protocol.pojo.Message;
import org.artJava.protocol.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(NodeExecutor.class);

	private Client mainClient;
	private String executorUID;

	private String masterIP;
	private int masterPort;

	// threads
	private Thread mainListener;

	public NodeExecutor(String mHost, int mPort) throws Exception {
		masterIP = mHost;
		masterPort = mPort;
		initExecutorUID();
		initMainListener();
		mainClient = new NettyClient();
	}

	private void connect() throws IOException, InterruptedException {
		mainClient.connectTo(masterIP, masterPort);
		LOGGER.info("Connected to master : " + masterIP + ":" + masterPort + ". ");
	}

	private void initExecutorUID() {
		if (executorUID == null) {
			executorUID = UUIDUtil.randomID();
		}
	}

	private void initMainListener() {
		mainListener = new Thread(new Runnable() {
			public void run() {
				while (!Thread.interrupted()) {
					handle(mainClient.receive());
				}
			}
		}, "NodeExecutor - main listener");
	}

	private void handle(Message msg) {
		if (msg != null) {
			System.out.println(msg);
		}
	}

	public void start() {
		LOGGER.info("Starting node executor... ");
		try {
			connect();
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to start master: ", e);
		}
		mainListener.start();
		LOGGER.info("Node executor started.");
	}

	public void stop() {
		LOGGER.info("Stopping node executor... ");
		try {
			mainListener.interrupt();
			mainListener.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if (mainClient != null) {
			mainClient.close();
			mainClient = null;
		}
		LOGGER.info("Node executor stopped.");
	}

	public void send(Message m) {
		mainClient.send(m);
	}

	public boolean isChannelOpen() {
		return mainClient.isConnected();
	}

	public static void main(String[] args) throws Exception {

		final NodeExecutor ne = new NodeExecutor("127.0.0.1", 8888);
		ne.start();

		new Thread(new Runnable() {
			public void run() {
				while (!Thread.interrupted()) {
					if (ne.isChannelOpen()) {
						try {
							TimeUnit.SECONDS.sleep(1);
							System.out.println("send...");

							Message message = new Message();
							Header header = new Header();
							header.setType(MessageType.MESSAGE.value());
							message.setHeader(header);
							message.setBody("aa");
							ne.send(message);
						} catch (Exception e) {
							Thread.currentThread().interrupt();
						}
					}
				}
			}
		}).start();

	}

}