package javacode;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestNioReadLine {
	public static void main(String[] args) {
		try {
			String path = "/Users/shuang/Downloads/test.txt";
			FileChannel inChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
			System.out.println("声明channel后position值:" + inChannel.position());
			Channels.newReader(inChannel, "utf-8");
			BufferedReader reader = new BufferedReader(Channels.newReader(inChannel, "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				System.out.println("读取上面内容后position值:" + inChannel.position());
				System.out.println("数据的长度:" + line.length());
			}
			reader.close();

			// 根据不同的字符集设置
			// Channels.newReader(channel, "ISO-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
