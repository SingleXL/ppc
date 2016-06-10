package org.artJava.protocol.pojo;

import java.io.Serializable;

public class Header implements Serializable {

	private static final long serialVersionUID = 1L;

	private int crcCode = 0xabef0101;
	private String executorUID;
	private byte type;

	public String getExecutorUID() {
		return executorUID;
	}

	public void setExecutorUID(String executorUID) {
		this.executorUID = executorUID;
	}

	public int getCrcCode() {
		return crcCode;
	}

	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
