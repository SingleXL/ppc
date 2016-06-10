package org.artJava.protocol.pojo;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private Header header;
	private Object body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message [header=" + header + ", body=" + body + "]";
	}

}
