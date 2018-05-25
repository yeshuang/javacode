package com.summer.jvm;

import java.util.ArrayList;
import java.util.List;

/*
 * Java堆溢出:
 * 	Java堆用于存储对象实例，只要不断地创建对象，并且保证GC Roots到对象之间有可达路径来避免垃圾回收机制清除这些对象，那么在对象数量到达最大堆的容量限制后就会产生内存溢出异常。
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class StudyHeapOOM {
	static class OOMObject {
	}

	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();

		while (true) {
			list.add(new OOMObject());
		}
	}
}
