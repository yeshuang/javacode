package com.summer.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

/*
 * 
 * 一、缓冲区（Buffer）：在Java NIO中负责数据的存取。缓冲充就是数组。用于存储不同数据类型的数据。
 * 
 * java根据数据类型不同（boolean除外），提供了相应类型的缓冲区：
 * 	ByteBuffer
 * 	CharBuffer
 * 	ShortBuffer
 * 	IntBuffer
 * 	LongBuffer
 * 	FloatBuffer
 *  DoubleBuffer
 *  
 * 上述缓冲区的管理方式几乎一致，通过allocat()获取缓冲区
 * 
 * 二、缓冲区存取数据的两个核心方法
 * 	put():存入数据到缓冲区中
 * 	get():获取缓冲区中的数据
 * 
 * 三、绥中区的四个核心属性
 * 	capacity:容量，表示缓冲区中最大存储数据的容量。一旦声明不能改变。
 * 	limit:界限，表示缓冲区中可以操作数据的大小。（limit后数据不能进行读写）
 * 	position:位置，表示缓冲区中即将操作数据的位置。
 * 	mark:标记，表示记录当前position的位置。可以通过reset()方法恢复到mark的位置。
 * 
 * 	0 <= mark <= position <= limit <= capacity
 *
 * 四、直接缓冲区与非直接缓冲区：
 * 	非直接缓冲区：通过 allocate() 方法分配缓冲区，将缓冲区建立在 JVM 的内存中
 * 	直接缓冲区：通过 allocateDirect() 方法分配直接缓冲区，将缓冲区建立在物理内存中。可以提高效率
 */
public class StudyNioBuffer {

	@Test
	public void test03() {
		// 分配直接缓冲区
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		// 查询当前缓冲区是否为直接缓冲区
		System.out.println("否为直接缓冲区:" + buf.isDirect());
	}

	@Test
	public void test02() {
		String str = "abcdef";
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(str.getBytes());
		buf.flip();
		byte[] dst = new byte[buf.limit()];

		buf.get(dst, 0, 2);
		System.out.println("读取缓冲区中0到2位置数据：" + new String(dst, 0, 2));
		System.out.println("读取数据后的position位置为：" + buf.position());

		// mark()标记
		buf.mark();
		System.out.println("----------调用mark()标记----------");

		buf.get(dst, 2, 2);
		System.out.println("读取缓冲区中2到2位置数据：" + new String(dst, 2, 2));
		System.out.println("读取数据后的position位置为：" + buf.position());

		// reset()恢复到mark的位置
		buf.reset();
		System.out.println("----------调用reset()恢复到mark的位置----------");
		System.out.println("调用reset()后的position位置为：" + buf.position());

		// 判断缓冲区中是否还有剩余数据
		if (buf.hasRemaining()) {
			// 获取缓冲区中可以操作的数量
			System.out.println("缓冲区中可以操作的数:" + buf.remaining());
		}
	}

	@Test
	public void test01() {
		// 1.分配一个指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		System.out.println("------------调用allocate(1024)后----------");
		System.out.println("position:" + buf.position());
		System.out.println("limit:" + buf.limit());
		System.out.println("capacity:" + buf.capacity());

		String str = "abcde";
		// 2.利用put()存入数据到缓冲区中
		buf.put(str.getBytes());
		System.out.println("------------调用put()后----------");
		System.out.println("position:" + buf.position());
		System.out.println("limit:" + buf.limit());
		System.out.println("capacity:" + buf.capacity());

		// 3.切换到读数据模式
		buf.flip();
		System.out.println("------------调用flip()后----------");
		System.out.println("position:" + buf.position());
		System.out.println("limit:" + buf.limit());
		System.out.println("capacity:" + buf.capacity());

		// 4.利用get()读取缓冲区中的数据
		byte[] dst = new byte[buf.limit()];
		buf.get(dst);
		System.out.println("获取缓冲区的数据:" + new String(dst, 0, dst.length));
		System.out.println("------------调用get()后----------");
		System.out.println("position:" + buf.position());
		System.out.println("limit:" + buf.limit());
		System.out.println("capacity:" + buf.capacity());

		// 5.rewind()可以重复读数据
		buf.rewind();
		System.out.println("------------调用rewind()后----------");
		System.out.println("position:" + buf.position());
		System.out.println("limit:" + buf.limit());
		System.out.println("capacity:" + buf.capacity());

		// 6.clear()清空缓冲区
		buf.clear();
		System.out.println("------------调用clear()后----------");
		System.out.println("position:" + buf.position());
		System.out.println("limit:" + buf.limit());
		System.out.println("capacity:" + buf.capacity());
		// 7.调用clear()空缓冲区后，缓冲区中的数据依然存在，此时处于“被遗忘”状态
		System.out.println("调用clear()空缓冲区后，缓冲区中的数据依然存在,获取缓冲区中第0个位置数据：" + (char) buf.get(0));

	}

}
