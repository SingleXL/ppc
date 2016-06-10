package org.artJava.protocol.network;

import java.io.IOException;

import org.artJava.protocol.pojo.Message;

public interface Client {

	void connectTo(String ipAddress, int port) throws IOException, InterruptedException;

	void send(Message msg);

	Message receive();

	void close();
}