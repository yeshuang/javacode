package com.summer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/*
 * 服务端反馈阻塞式NIO
 *
 */
public class StudyBlockingNIO02 {

	@Test
	public void client() {
		try {
			SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

			FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

			ByteBuffer buf = ByteBuffer.allocate(1024);

			while (inChannel.read(buf) != -1) {
				buf.flip();
				sChannel.write(buf);
				buf.clear();
			}

			// 测试程序是阻塞式的，为了达到效果，使用如下方法关闭输出流，关于输出流，在下面有说明
			sChannel.shutdownOutput();

			// 接受服务端的反馈
			int len = 0;
			while ((len = sChannel.read(buf)) != -1) {
				buf.flip();
				System.out.println("服务反馈:" + new String(buf.array(), 0, len));
				buf.clear();
			}

			inChannel.close();
			sChannel.close();
			System.out.println("--------请求服务已完成-------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void server() {
		try {
			ServerSocketChannel ssChannel = ServerSocketChannel.open();

			FileChannel outChannel = FileChannel.open(Paths.get("6.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

			ssChannel.bind(new InetSocketAddress(9898));

			SocketChannel sChannel = ssChannel.accept();

			ByteBuffer buf = ByteBuffer.allocate(1024);

			while (sChannel.read(buf) != -1) {
				buf.flip();
				outChannel.write(buf);
				buf.clear();
			}

			// 发送反馈给客户端
			buf.put("收到请求并处理完成".getBytes());
			buf.flip();
			sChannel.write(buf);

			sChannel.close();
			outChannel.close();
			ssChannel.close();
			System.out.println("--------服务已启动-------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 虽然在大多数的时候可以直接使用Socket类或输入输出流的close方法关闭网络连接，但有时我们只希望关闭OutputStream或InputStream，<br>
	 * 而在关闭输入输出流的同时，并不关闭网络连接。这就需要用到Socket类的另外两个方法：shutdownInput和shutdownOutput，这两个方法只关闭相应的输入、输出流，<br>
	 * 而它们并没有同时关闭网络连接的功能。和isClosed、isConnected方法一样，Socket类也提供了两个方法来判断Socket对象的输入、输出流是否被关闭，<br>
	 * 这两个方法是isInputShutdown()和isOutputShutdown()。下面的代码演示了只关闭输入、输出流的过程：
	 */
	public void testSocket() {
		Socket socket;
		try {
			socket = new Socket("www.ptpress.com.cn", 80);
			printState(socket);

			socket.shutdownInput();
			printState(socket);

			socket.shutdownOutput();
			printState(socket);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printState(Socket socket) {
		System.out.println("isInputShutdown:" + socket.isInputShutdown());
		System.out.println("isOutputShutdown:" + socket.isOutputShutdown());
		System.out.println("isClosed:" + socket.isClosed());
		System.out.println();
	}
}
