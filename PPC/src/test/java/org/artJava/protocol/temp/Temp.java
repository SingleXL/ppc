package org.artJava.protocol.temp;

import org.artJava.protocol.util.UUIDUtil;
import org.glassfish.jersey.server.model.Suspendable;

class a {

	private int a = 10;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

}

class b extends a {

	private int b = 3;

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

}

public class Temp {
	
	public static void main(String[] args) {
		
		System.out.println(UUIDUtil.getExecutorUID());
	
	}

}






















