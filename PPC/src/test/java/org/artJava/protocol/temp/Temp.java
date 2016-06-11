package org.artJava.protocol.temp;

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
		
		
		b bb = new b();
		bb.setA(123);
		a aa = (a)bb;
		
		System.out.println(aa.getA());
	}

}






















