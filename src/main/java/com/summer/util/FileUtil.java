package com.summer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

	public static void main(String[] args) {
		System.out.println("ready go !!! \n");
		long st = System.currentTimeMillis();
		try {
			readyGo();
			// testMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n all files have been raped -_- expenditure time (" + (System.currentTimeMillis() - st) + ")ms");
	}

	/**
	 * 测试方法
	 * @throws Exception
	 */
	public static void testMethod() throws Exception {
		FileInputStream fis = new FileInputStream("/Users/shuang/git/rms-js/rms-dao-js/src/main/resources/sqlmap/GatewayBusinessDetailJsMapper.xml");
		BufferedReader buf = new BufferedReader(new InputStreamReader(fis));
		String s = null;
		StringBuffer cont = new StringBuffer();
		while ((s = buf.readLine()) != null) {
			cont.append(s);
		}
		buf.close();
		Pattern pattern = Pattern.compile("<select [\\s\\S]*?>[\\s\\S]*?</select>");// <select [\\s\\S]*?>[\\s\\S(order by)+\\s\\S]*?</select>
		Matcher matcher = pattern.matcher(cont);
		StringBuffer sql = new StringBuffer();
		while (matcher.find()) {
			String pm = matcher.group();
			System.out.println(pm);
			if (((pm.indexOf("distinct") > -1 || pm.indexOf("DISTINCT") > -1) && (pm.indexOf("order by") > -1 || pm.indexOf("ORDER BY") > -1)) || (pm.indexOf("group by") > -1 || pm.indexOf("GROUP BY") > -1)) {
				sql.append(pm);
			}
		}
		System.out.println("-------------------------- \n pattern contents: " + sql);
	}

	public static void readyGo() throws Exception {
		Set<File> filelist = getFileList("/Users/shuang/git/rms-js/rms-dao-js/src/main/resources/sqlmap/");
		if (filelist.isEmpty()) {
			System.out.println("----not find files----");
			System.exit(0);
		}
		Iterator<File> it = filelist.iterator();
		while (it.hasNext()) {
			File file = it.next();
			Pattern pattern = Pattern.compile("<select [\\s\\S]*?>[\\s\\S]*?</select>");// <select [\\s\\S]*?>[\\s\\S(order by)+\\s\\S]*?</select>
			Matcher matcher = pattern.matcher(getFileContents(file.getPath()));
			StringBuffer sql = new StringBuffer();
			while (matcher.find()) {
				String pm = matcher.group();
				if (pm.isEmpty()) {
					System.out.println("----not pattern contents----");
					System.exit(0);
				}
				if (((pm.indexOf("distinct") > -1 || pm.indexOf("DISTINCT") > -1) && (pm.indexOf("order by") > -1 || pm.indexOf("ORDER BY") > -1)) || (pm.indexOf("group by") > -1 || pm.indexOf("GROUP BY") > -1)) {
					sql.append(pm);
				}
			}
			writerInFile("/Users/shuang/Downloads/orderby-group-distinct/" + file.getName(), sql.toString());
			// System.out.println(file.getName() + " have been raped -_- -_-");
		}
	}

	/**
	 * 获取文件内容
	 * @param path
	 *            文件绝对路径
	 * @return
	 * @throws Exception
	 */
	public static String getFileContents(String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		BufferedReader buf = new BufferedReader(new InputStreamReader(fis));
		String s = null;
		StringBuffer cont = new StringBuffer();
		while ((s = buf.readLine()) != null) {
			cont.append(s);
		}
		buf.close();
		return cont.toString();
	}

	/**
	 * 内容写入文件
	 * @param path
	 *            带文件名后缀的绝对文件路径
	 * @param cont
	 *            内容
	 * @throws Exception
	 */
	public static void writerInFile(String path, String cont) throws Exception {
		File file = new File(path);
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(cont);
		bw.close();
	}

	/**
	 * 获取文件目录下所有文件路径
	 * @param strPath
	 *            文件目录
	 * @return
	 */
	public static Set<File> getFileList(String strPath) {
		Set<File> filelist = new HashSet<File>();
		File dir = new File(strPath);
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
				if (files[i].isDirectory()) { // 判断是文件还是文件夹
					getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
				} else if (fileName.endsWith("xml")) { // 判断文件名是否以.xml结尾
					String strFileName = files[i].getAbsolutePath();
					// System.out.println("strFileName: " + strFileName);
					filelist.add(files[i]);
				} else {
					continue;
				}
			}

		}
		return filelist;
	}

}
