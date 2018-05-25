package javacode;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		String s = "http://172.19.10.1";
		System.out.println(s.endsWith(File.separator));
		if (!s.endsWith(File.separator)) {
			System.out.println(s);
			s = s + File.separator;
			System.out.println(s);
		}
	}

}
