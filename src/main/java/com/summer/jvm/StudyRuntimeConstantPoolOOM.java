package com.summer.jvm;

import java.util.ArrayList;
import java.util.List;

/*
 * 运行时常量池溢出:
 * 	String.intern（）是一个Native方法，它的作用是：如果字符串常量池中已经包含一个等
	于此String对象的字符串，则返回代表池中这个字符串的String对象；否则，将此String对象包
	含的字符串添加到常量池中，并且返回此String对象的引用。 在JDK 1.6及之前的版本中，由
	于常量池分配在永久代内，我们可以通过-XX：PermSize和-XX：MaxPermSize限制方法区大
	小，从而间接限制其中常量池的容量
 * VM Args：-XX:PermSize=10M -XX:MaxPermSize=10M
 * 	
 */
public class StudyRuntimeConstantPoolOOM {
	public static void main(String[] args) {
		// 使用List保持着常量池引用，避免Full GC回收常量池行为
		List<String> list = new ArrayList<String>();
		// 10MB的PermSize在integer范围内足够产生OOM了
		int i = 0;
		while (true) {
			list.add(String.valueOf(i++).intern());
		}
	}

}
