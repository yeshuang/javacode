package com.summer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

import com.summer.util.DateUtils;

/*
 * 一、使用 NIO 完成网络通信的三个核心：
 * 
 * 1. 通道（Channel）：负责连接
 * 		
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 *  * 注：SocketChannel与ServerSocketChannel用于TCP/IP，DatagramChannel用于UDP
 * 
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 * 
 * 2. 缓冲区（Buffer）：负责数据的存取
 * 
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 * 
 */
@SuppressWarnings("resource")
public class StudyNonBlockingNIO02 {

	@Test
	public void send() {
		try {
			// 1.获取通道
			DatagramChannel dc = DatagramChannel.open();

			// 2.切换非阻塞模式
			dc.configureBlocking(false);

			// 3.分配指定大小的缓存区
			ByteBuffer buf = ByteBuffer.allocate(1024);

			// 4.获取输入数据
			Scanner scan = new Scanner(System.in);
			while (scan.hasNext()) {
				String str = scan.next();
				buf.put((DateUtils.format(new Date()) + ":" + str).getBytes());
				buf.flip();
				dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));
			}
			dc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void receive() throws IOException {
		// 获取通道
		DatagramChannel dc = DatagramChannel.open();

		// 2.切换非阻塞模式
		dc.configureBlocking(false);

		// 3.绑定连接
		dc.bind(new InetSocketAddress(9898));

		// 4.获取选择器
		Selector selector = Selector.open();

		// 5.将通道注册到选择器上，并且指定"监听读事件"
		dc.register(selector, SelectionKey.OP_READ);

		// 6.轮询式的获取选择器上已经"准备就绪"的事件
		while (selector.select() > 0) {

			// 7. 获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

			while (it.hasNext()) {
				// 8. 获取准备“就绪”的是事件
				SelectionKey sk = it.next();

				if (sk.isReadable()) {
					ByteBuffer buf = ByteBuffer.allocate(1024);

					dc.receive(buf);
					buf.flip();
					System.out.println(new String(buf.array(), 0, buf.limit()));
					buf.clear();
				}
			}

			it.remove();
		}
	}
}
