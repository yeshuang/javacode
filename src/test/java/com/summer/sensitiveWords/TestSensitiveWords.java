package com.summer.sensitiveWords;

import org.junit.Test;

public class TestSensitiveWords {

	@Test
	public void testSensitiveWords() {
		String cont = "微信hao Y 3!$%P";
		String resutlCont = WordFilter.doFilter(cont);
		System.out.println("resutlCont:" + resutlCont);
	}
}
