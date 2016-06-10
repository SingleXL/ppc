package org.artJava.protocol.constant;

/**
 * @author artJava
 * @date 2016年6月6日
 * @version 1.0
 */
public enum MessageType {

	SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1), MESSAGE((byte) 2),
	LOGIN_REQ((byte) 3), LOGIN_RESP((byte) 4), 
	HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6);

	private byte value;

	private MessageType(byte value) {
		this.value = value;
	}

	public byte value() {
		return this.value;
	}
}
