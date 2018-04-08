package com.summer.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/*
 * 一、通道（Channel）：用于源节点与目标节点的连接。在 Java NIO 中负责缓冲区中数据的传输。Channel 本身不存储数据，因此需要配合缓冲区进行传输。
 * 
 * 二、通道的主要实现类
 * 	java.nio.channels.Channel 接口：
 * 		|--FileChannel
 * 		|--SocketChannel
 * 		|--ServerSocketChannel
 * 		|--DatagramChannel
 * 
 * 注：FileChannel用于本地，SocketChannel与ServerSocketChannel用于TCP/IP，DatagramChannel用于UDP
 * 
 * 三、获取通道
 * 1. Java 针对支持通道的类提供了 getChannel() 方法
 * 		本地 IO：
 * 		FileInputStream/FileOutputStream
 * 		RandomAccessFile
 * 
 * 		网络IO：
 * 		Socket
 * 		ServerSocket
 * 		DatagramSocket
 * 		
 * 2. 在 JDK 1.7 中的 NIO.2 针对各个通道提供了静态方法 open()
 * 3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 * 
 * 四、通道之间的数据传输
 * transferFrom()
 * transferTo()
 * 
 * 五、分散(Scatter)与聚集(Gather)
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 * 
 * 六、字符集：Charset
 * 编码：字符串 -> 字节数组
 * 解码：字节数组  -> 字符串
 * 
 */
public class StudyNioChannel {

	@Test
	// 分散(Scatter)与聚集(Gather)
	public void test04() {
		try {
			RandomAccessFile inraf = new RandomAccessFile("1.txt", "rw");
			RandomAccessFile outraf = new RandomAccessFile("2.txt", "rw");

			// 1.获取通道
			FileChannel inChannel = inraf.getChannel();
			FileChannel outChannel = outraf.getChannel();
			// 2.分配指定大小的缓冲区
			ByteBuffer buf01 = ByteBuffer.allocate(5);
			ByteBuffer buf02 = ByteBuffer.allocate(10);
			ByteBuffer buf03 = ByteBuffer.allocate(15);
			ByteBuffer buf04 = ByteBuffer.allocate(1024);

			// 3.分散读取
			ByteBuffer[] bufs = { buf01, buf02, buf03, buf04 };
			inChannel.read(bufs);

			// 4.切换到读数据模式
			for (ByteBuffer byteBuffer : bufs) {
				byteBuffer.flip();
			}

			// 5.聚集写入
			outChannel.write(bufs);

			// 6.关闭通道
			inChannel.close();
			outChannel.close();
			inraf.close();
			outraf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	// 通道之间的数据传输（直接缓冲区）
	public void test03() {
		try {
			FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
			// StandardOpenOption.CREATE:存在则替换 StandardOpenOption.CREATE_NEW:存在则报错
			FileChannel outChannel = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

			// 道之间的数据传输
			// inChannel.transferTo(0, inChannel.size(), outChannel);
			outChannel.transferFrom(inChannel, 0, inChannel.size());

			// 关闭通道
			inChannel.close();
			outChannel.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	// 使用直接缓冲区完成文件复制（内存映射文件）
	public void test02() {
		try {
			FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
			// StandardOpenOption.CREATE:存在则替换 StandardOpenOption.CREATE_NEW:存在则报错
			FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

			// 内存映射文件
			MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
			MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, 0, inMappedBuf.limit());

			// 直接对缓存区进行数据的读写操作
			byte[] dst = new byte[inMappedBuf.limit()];
			inMappedBuf.get(dst);
			outMappedBuf.put(dst);

			// 关闭通道
			inChannel.close();
			outChannel.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	// 利用通道完成文件复制（非直接缓存区）
	public void test01() {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			fis = new FileInputStream("1.jpg");
			fos = new FileOutputStream("2.jpg");
			// 1.获取通道
			inChannel = fis.getChannel();
			outChannel = fos.getChannel();
			// 2.分配指定大小的非直接缓冲区
			ByteBuffer buf = ByteBuffer.allocate(1024);
			// 3.将通道中的数据存入缓冲区中
			while (inChannel.read(buf) != -1) {
				// 4.切换到读数据模式
				buf.flip();
				// 5.将缓通道冲区的数据写入中
				outChannel.write(buf);
				// 6.清空缓冲区
				buf.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
